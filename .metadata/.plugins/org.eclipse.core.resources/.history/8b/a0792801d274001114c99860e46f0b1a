package com.tpfinal.batallanaval.controller;

import com.tpfinal.batallanaval.game.*;
import com.tpfinal.batallanaval.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIController implements GameListener {
    private final Game game;
    private final boolean isPlayer1;
    private final Random random;
    
    // La IA necesita recordar dónde disparó para no repetir la misma coordenada a lo tonto
    private final List<Position> firedShots;

    public AIController(Game game, boolean isPlayer1) {
        this.game = game;
        this.isPlayer1 = isPlayer1;
        this.random = new Random();
        this.firedShots = new ArrayList<>();
    }

    @Override
    public void onGameStateChanged(GameSnapshot snapshot) {
        // --- FASE 1: COLOCACIÓN DE BARCOS ---
        if (snapshot.state == GameState.PLACING_SHIPS) {
            int myShipCount = isPlayer1 ? snapshot.player1Ships.size() : snapshot.player2Ships.size();
            int totalShips = game.getConfig().getShipCount();

            // Si a la IA le faltan barcos por colocar
            if (myShipCount < totalShips) {
                int size = game.getConfig().getShipLengths()[myShipCount];
                Player me = isPlayer1 ? game.getPlayer1() : game.getPlayer2();
                
                boolean placed = false;
                // Intentar coordenadas al azar hasta que el Juego lo acepte
                while (!placed) {
                    int x = random.nextInt(game.getConfig().getWidth());
                    int y = random.nextInt(game.getConfig().getHeight());
                    Direction dir = random.nextBoolean() ? Direction.HORIZONTAL : Direction.VERTICAL;
                    
                    Ship randomShip = new Ship("BotShip", size, new Position(x, y), dir);
                    placed = game.placeShip(me, randomShip);
                }
            }
        }
        
        // --- FASE 2: DISPAROS ---
        else if (snapshot.state == GameState.PLAYING && snapshot.isPlayer1Turn == this.isPlayer1) {
            
            // Creamos un hilo aparte para no congelar la UI de la consola mientras el Bot "piensa"
            new Thread(() -> {
                try {
                    Thread.sleep(1500); // El bot "piensa" durante 1.5 segundos
                } catch (InterruptedException e) {}

                Position target;
                // Buscar una coordenada aleatoria en la que NO hayamos disparado antes
                do {
                    int x = random.nextInt(game.getConfig().getWidth());
                    int y = random.nextInt(game.getConfig().getHeight());
                    target = new Position(x, y);
                } while (firedShots.contains(target));

                firedShots.add(target);
                
                // ¡Fuego!
                game.processShot(target);
                
            }).start();
        }
    }

    // Los demás eventos los dejamos vacíos porque la IA no necesita imprimir nada,
    // de eso ya se encarga la ConsoleUI.
    @Override public void onShipPlaced(Ship ship, int remainingCount) {}
    @Override public void onError(String errorMessage) {}
    @Override public void onShotFired(Position pos, ShotResult result, boolean isPlayer1Turn) {}
}
