package com.tpfinal.batallanaval.view;

import com.tpfinal.batallanaval.controller.PlayerController;
import com.tpfinal.batallanaval.game.GameListener;
import com.tpfinal.batallanaval.model.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements GameListener {
    private final PlayerController controller;
    private final GameConfig config; // Usamos config para saber el tamaño al dibujar
    private final Scanner scanner;
    private GameSnapshot currentSnapshot;

    // Ahora la UI recibe su Controlador (Puente) y la Configuración (para dibujar)
    private final Boolean fixedPerspectiveP1; 

    public ConsoleUI(PlayerController controller, GameConfig config, Boolean fixedPerspectiveP1) {
        this.controller = controller;
        this.config = config;
        this.fixedPerspectiveP1 = fixedPerspectiveP1;
        this.scanner = new Scanner(System.in);
    }

    // --- MÉTODOS DE RECEPCIÓN (EVENTOS) ---

    @Override
    public void onGameStateChanged(GameSnapshot snapshot) {
        this.currentSnapshot = snapshot;
        System.out.println("\n=========================================");
        System.out.println("ESTADO: " + snapshot.state);
        
        int w = config.getWidth();
        int h = config.getHeight();

        // MAGIA CORREGIDA: Calculamos quién debe ver la pantalla
        boolean viewAsP1;
        if (fixedPerspectiveP1 != null) {
            viewAsP1 = fixedPerspectiveP1; // Perspectiva fija (Red o vs IA)
        } else {
            // Perspectiva dinámica (Hotseat)
            if (snapshot.state == GameState.PLACING_SHIPS) {
                // Si al J1 le faltan barcos, la cámara es del J1. Si ya terminó, la cámara gira al J2.
                viewAsP1 = (snapshot.player1Ships.size() < config.getShipCount());
            } else {
                // Si estamos jugando, la cámara gira según de quién es el turno
                viewAsP1 = snapshot.isPlayer1Turn;
            }
        }

        if (snapshot.state == GameState.PLACING_SHIPS) {
            // Agregamos de quién es el turno en el título para que no haya confusión
            System.out.println("--- TURNO DE COLOCACIÓN: " + (viewAsP1 ? "JUGADOR 1" : "JUGADOR 2") + " ---");
            
            if (viewAsP1) {
                drawBoard(snapshot.player1Ships, snapshot.player1MissedShots, w, h, true);
            } else {
                drawBoard(snapshot.player2Ships, snapshot.player2MissedShots, w, h, true);
            }
            System.out.println("\n> Escribe: X Y Direccion(H/V) -> ej: '2 3 H'");
        }
        else if (snapshot.state == GameState.PLAYING) {
        	System.out.println("TURNO DE: " + (snapshot.isPlayer1Turn ? "Jugador 1" : "Jugador 2"));
            
            System.out.println("\n--- MI FLOTA ---");
            if (viewAsP1) drawBoard(snapshot.player1Ships, snapshot.player1MissedShots, w, h, true);
            else drawBoard(snapshot.player2Ships, snapshot.player2MissedShots, w, h, true);
            
            System.out.println("\n--- RADAR ENEMIGO ---");
            if (viewAsP1) drawBoard(snapshot.player2Ships, snapshot.player2MissedShots, w, h, false);
            else drawBoard(snapshot.player1Ships, snapshot.player1MissedShots, w, h, false);
            
            // Bloquear el mensaje si tenemos perspectiva fija y no es nuestro turno
            boolean isMyTurn = (fixedPerspectiveP1 == null) || (snapshot.isPlayer1Turn == fixedPerspectiveP1);
            if (isMyTurn) {
                System.out.println("\n> ¡Es tu turno! Escribe coordenadas para disparar:");
            } else {
                System.out.println("\n> Esperando que el enemigo dispare...");
            }
        } 
        else if (snapshot.state == GameState.FINISHED) {
            System.out.println("¡PARTIDA TERMINADA! Ganador: " + (snapshot.isPlayer1Turn ? "Jugador 1" : "Jugador 2"));
        }
    }

    @Override
    public void onShipPlaced(Ship ship, int remainingCount) {
        System.out.println("✅ ¡Barco colocado! Faltan colocar: " + remainingCount);
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println("❌ Error: " + errorMessage);
    }

    @Override
    public void onShotFired(Position pos, ShotResult result, boolean wasPlayer1) {
        String tirador = wasPlayer1 ? "Jugador 1" : "Bot Enemigo";
        System.out.print(">>> " + tirador + " dispara en (" + pos.getX() + "," + pos.getY() + ") -> ");
        
        switch (result) {
            case MISS: System.out.println("💦 ¡AGUA!"); break;
            case HIT: System.out.println("🔥 ¡IMPACTO!"); break;
            case SUNK: System.out.println("💥 ¡TOCADO Y HUNDIDO!"); break;
        }
    }

    private void drawBoard(List<Ship> ships, List<Position> misses, int width, int height, boolean showIntactShips) {
        System.out.print("  ");
        for (int x = 0; x < width; x++) System.out.print(x + " ");
        System.out.println();

        for (int y = 0; y < height; y++) {
            System.out.print(y + " ");
            for (int x = 0; x < width; x++) {
                Position currentPos = new Position(x, y);
                String symbol = "~ ";

                if (misses.contains(currentPos)) symbol = "O ";

                for (Ship ship : ships) {
                    for (Cell part : ship.getParts()) {
                        if (part.getPosition().equals(currentPos)) {
                            if (part.isHit()) symbol = "X ";
                            else if (showIntactShips) symbol = "S ";
                        }
                    }
                }
                System.out.print(symbol);
            }
            System.out.println();
        }
    }

    // --- MÉTODO DE ENVÍO (INPUTS) ---

    public void startInputLoop() {
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) break;

         // Bloquear input solo si tenemos cámara fija y no es nuestro turno
            boolean isMyTurn = (fixedPerspectiveP1 == null) || (currentSnapshot.isPlayer1Turn == fixedPerspectiveP1);
            if (currentSnapshot.state == GameState.PLAYING && !isMyTurn) {
                System.out.println("Tranquilo, es el turno del enemigo...");
                continue;
            }

            try {
                String[] parts = input.split(" ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                if (currentSnapshot.state == GameState.PLACING_SHIPS) {
                    // Solo pasamos datos crudos al controlador
                    Direction dir = parts[2].toUpperCase().startsWith("H") ? Direction.HORIZONTAL : Direction.VERTICAL;
                    controller.attemptPlaceShip(x, y, dir);
                    
                } else if (currentSnapshot.state == GameState.PLAYING) {
                    // Solo pasamos datos crudos al controlador
                    controller.fireShot(x, y);
                }
            } catch (Exception e) {
                System.out.println("❌ Formato inválido. Sigue las instrucciones de la pantalla.");
            }
        }
    }
}