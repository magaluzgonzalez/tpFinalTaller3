package com.tpfinal.batallanaval.view;

import com.tpfinal.batallanaval.controller.PlayerController;
import com.tpfinal.batallanaval.game.GameListener;
import com.tpfinal.batallanaval.model.*;

import java.util.Scanner;

public class ConsoleUI implements GameListener {
    private final PlayerController controller;
    private final GameConfig config; 
    private final Scanner scanner;
    private GameSnapshot currentSnapshot;
    private final Boolean fixedPerspectiveP1; 
    
    // Bandera para asegurarnos de tirar el cartel de inicio de juego una sola vez
    private boolean cartelJuegoIniciadoMostrado = false;

    public ConsoleUI(PlayerController controller, GameConfig config, Boolean fixedPerspectiveP1) {
        this.controller = controller;
        this.config = config;
        this.fixedPerspectiveP1 = fixedPerspectiveP1;
        this.scanner = new Scanner(System.in);
    }

    // --- MÉTODOS DE RECEPCIÓN (EVENTOS SIMPLIFICADOS) ---

    @Override
    public void onGameStateChanged(GameSnapshot snapshot) {
        this.currentSnapshot = snapshot;
        
        // Determinar qué perspectiva visual teórica hay activa (para los carteles de turno)
        boolean viewAsP1 = (fixedPerspectiveP1 != null) ? fixedPerspectiveP1 : 
            (snapshot.state == GameState.PLACING_SHIPS ? (snapshot.player1Ships.size() < config.getShipCount()) : snapshot.isPlayer1Turn);

        if (snapshot.state == GameState.PLACING_SHIPS) {
            System.out.println("\n=========================================");
            System.out.println("ESTADO: COLOCACIÓN DE BARCOS");
            System.out.println("-> Coloca tus piezas en la botonera gráfica.");
            System.out.println("-> Turno de colocación: " + (viewAsP1 ? "JUGADOR 1" : "JUGADOR 2"));
            System.out.println("> Escribe en la barra inferior: X Y Direccion(H/V) -> ej: '2 3 H'");
        }
        else if (snapshot.state == GameState.PLAYING) {
            // --- NUEVO CARTEL DE TRANSICIÓN DE FASE EN EL CLIENTE ---
            if (!cartelJuegoIniciadoMostrado) {
                System.out.println("\n=========================================");
                System.out.println("🎉 ¡YA ESTÁN TODOS LOS BARCOS PUESTOS, A JUGAR! 🎉");
                System.out.println("=========================================");
                cartelJuegoIniciadoMostrado = true;
            }

            // Identificar el turno de forma clara y explícita para el cliente
            String stringTurno;
            if (snapshot.isPlayer1Turn) {
                stringTurno = "Jugador 1 (Host)";
            } else {
                stringTurno = "Jugador 2 (Tú)";
            }

            System.out.println("\n-----------------------------------------");
            System.out.println("⚔️ TURNO ACTUAL: " + stringTurno);
            
            // Evaluamos si es el turno del cliente en base a su perspectiva fija (Jugador 2 -> fixedPerspectiveP1 == false)
            boolean esMiTurnoEnRed = (fixedPerspectiveP1 == null) || (snapshot.isPlayer1Turn == fixedPerspectiveP1);
            
            if (esMiTurnoEnRed) {
                System.out.println("> ¡Es tu turno! Haz clic en el radar enemigo para disparar.");
            } else {
                System.out.println("> Esperando que el enemigo (Host) ejecute su disparo...");
            }
        }
     }

    @Override
    public void onShipPlaced(Ship ship, int remainingCount) {
        System.out.println("✅ ¡Barco colocado exitosamente! Faltan colocar: " + remainingCount);
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println("❌ Error táctico: " + errorMessage);
    }

    @Override
    public void onShotFired(Position pos, ShotResult result, boolean wasPlayer1) {
        // Mapear quién ejecutó el tiro de forma prolija para la bitácora
        String tirador;
        if (wasPlayer1) {
            tirador = "Jugador 1";
        } else {
            tirador = (fixedPerspectiveP1 != null && fixedPerspectiveP1) ? "IA Enemiga" : "Jugador 2";
        }
        
        System.out.print(">>> " + tirador + " dispara en (" + pos.getX() + "," + pos.getY() + ") -> ");
        
        switch (result) {
            case MISS -> System.out.println("💦 ¡AGUA!");
            case HIT -> System.out.println("🔥 ¡IMPACTO!");
            case SUNK -> System.out.println("💥 ¡TOCADO Y HUNDIDO!");
        }
    }

    // --- ELIMINAMOS EL MÉTODO DRAWBOARD POR COMPLETO ---

    // --- MÉTODO DE ENVÍO (INPUTS CONSERVADO) ---
    public void startInputLoop() {
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) break;

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
                    Direction dir = parts[2].toUpperCase().startsWith("H") ? Direction.HORIZONTAL : Direction.VERTICAL;
                    controller.attemptPlaceShip(x, y, dir);
                } else if (currentSnapshot.state == GameState.PLAYING) {
                    controller.fireShot(x, y);
                }
            } catch (Exception e) {
                System.out.println("❌ Formato inválido.");
            }
        }
    }
}