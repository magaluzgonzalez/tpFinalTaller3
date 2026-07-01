package com.tpfinal.batallanaval.UI;

import com.tpfinal.batallanaval.controller.PlayerController;
import com.tpfinal.batallanaval.game.GameListener;
import com.tpfinal.batallanaval.model.*;
import com.tpfinal.batallanaval.view.ConsoleUI;

import javax.swing.Timer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.SwingUtilities;

public class GameUIAdapter implements GameListener {
    private final ConsoleUI consoleUI; 
    private final SwingUI vistaGrafica;
    private final PlayerController controller;
    private final GameConfig config;
    private final Boolean fixedPerspectiveP1;
    private final PerspectiveManager perspectiveManager;
    
    private final ByteArrayOutputStream capturadorBuffer;
    private final PrintStream printStreamAlternativo;
    private final PrintStream standardOut;
    private GameSnapshot ultimoSnapshot;

    private boolean bloqueadoPorTransicion = false;

    public GameUIAdapter(PlayerController controller, GameConfig config, Boolean fixedPerspectiveP1) {
        this.controller = controller;
        this.config = config;
        this.fixedPerspectiveP1 = fixedPerspectiveP1;

        this.vistaGrafica = new SwingUI("Battleship - Control", config.getWidth(), config.getHeight(), this::mapearClickMouse);
        this.consoleUI = new ConsoleUI(controller, config, fixedPerspectiveP1);

        this.capturadorBuffer = new ByteArrayOutputStream();
        this.printStreamAlternativo = new PrintStream(capturadorBuffer);
        this.standardOut = System.out;

        this.perspectiveManager = new PerspectiveManager(config, fixedPerspectiveP1);
    }

    private void mapearClickMouse(int x, int y, boolean esMiFlota, boolean esClicIzquierdo) {
        if (ultimoSnapshot == null || bloqueadoPorTransicion) return;

        // FASE 1: Colocación de barcos
        if (ultimoSnapshot.state == GameState.PLACING_SHIPS) {
            boolean leTocaColocarAP1 = (fixedPerspectiveP1 != null) ? fixedPerspectiveP1 : 
                (ultimoSnapshot.player1Ships.size() < config.getShipCount());

            if ((leTocaColocarAP1 && esMiFlota) || (!leTocaColocarAP1 && !esMiFlota)) {
                Direction dir = esClicIzquierdo ? Direction.HORIZONTAL : Direction.VERTICAL;
                ejecutarAccionYActualizarUI(() -> controller.attemptPlaceShip(x, y, dir));
                forzarRefrescoVisual();
            } else {
                ejecutarAccionYActualizarUI(() -> System.out.println("❌ No podés colocar barcos en el tablero del rival."));
            }
            return;
        }
        
        // FASE 2: Disparos
        if (ultimoSnapshot.state == GameState.PLAYING && esClicIzquierdo) {
            
            if (fixedPerspectiveP1 != null) {
                // --- MODO RED / IA ---
                boolean miTurnoRed = (fixedPerspectiveP1 && ultimoSnapshot.isPlayer1Turn) || (!fixedPerspectiveP1 && !ultimoSnapshot.isPlayer1Turn);
                boolean clickRadarRed = (fixedPerspectiveP1 && !esMiFlota) || (!fixedPerspectiveP1 && esMiFlota);

                if (miTurnoRed) {
                    if (clickRadarRed) {
                        ejecutarAccionYActualizarUI(() -> controller.fireShot(x, y));
                        forzarRefrescoVisual();
                    } else {
                        // AGREGADO: Alerta de autodisparo en modo Red
                        ejecutarAccionYActualizarUI(() -> System.out.println("❌ ¡Disparo inválido! No podés dispararle a tu propia flota de defensa."));
                    }
                } else {
                    ejecutarAccionYActualizarUI(() -> System.out.println("⚠️ Esperá, es el turno del enemigo remoto."));
                }
            } else {
                // --- MODO HOTSEAT LOCAL (PVP) ---
                boolean clickValidoLocal = (ultimoSnapshot.isPlayer1Turn && !esMiFlota) || (!ultimoSnapshot.isPlayer1Turn && esMiFlota);

                if (clickValidoLocal) {
                    // 1. CAPTURA CRÍTICA: Guardamos de quién es el turno ANTES del disparo
                    final boolean turnoDelQueDisparo = ultimoSnapshot.isPlayer1Turn;
                    
                    // 2. Ejecutamos el tiro en el motor (esto cambia el turno internamente en el modelo)
                    ejecutarAccionYActualizarUI(() -> controller.fireShot(x, y));
                    
                    // 3. Forzamos un repintado intermedio usando el turno viejo de forma manual
                    // Esto hace que el casillero se pinte INSTANTÁNEAMENTE en azul o rojo sin cambiar de pantalla
                    SwingUtilities.invokeLater(() -> {
                        perspectiveManager.aplicarPerspectivaConTurnoFijo(ultimoSnapshot, vistaGrafica, turnoDelQueDisparo);
                    });

                    // 4. Si el juego ya terminó, no congelamos ni hacemos esperar a nadie
                    if (ultimoSnapshot.state == GameState.FINISHED) {
                        return;
                    }

                    // 5. Bloqueamos interacciones y abrimos la ventana de espera de 3 segundos
                    bloqueadoPorTransicion = true;
                    vistaGrafica.printLine("\n=========================================");
                    vistaGrafica.printLine("⚠️ ¡TIRO REGISTRADO! CAMBIANDO DE JUGADOR...");
                    vistaGrafica.printLine("Por favor, cdele la silla al siguiente jugador.");
                    vistaGrafica.printLine("=========================================\n");

                    Timer timer = new Timer(2000, e -> {
                        bloqueadoPorTransicion = false; // Liberamos el mouse
                        forzarRefrescoVisual();         // Recién ACÁ la pantalla rota y revela los barcos del otro
                    });
                    timer.setRepeats(false);
                    timer.start();
                    
                } else {
                    ejecutarAccionYActualizarUI(() -> System.out.println("❌ ¡Disparo inválido! No podés dispararle a tu propia flota de defensa."));
                }
            }
        }
    }
    private void forzarRefrescoVisual() {
        if (ultimoSnapshot != null) {
            SwingUtilities.invokeLater(() -> perspectiveManager.aplicarPerspectiva(ultimoSnapshot, vistaGrafica));
        }
    }

    private synchronized void ejecutarAccionYActualizarUI(Runnable accionEventual) {
        System.setOut(printStreamAlternativo);
        try {
            accionEventual.run();
            System.out.flush();
        } finally {
            System.setOut(standardOut);
        }
        String textoGenerado = capturadorBuffer.toString();
        capturadorBuffer.reset();

        if (!textoGenerado.trim().isEmpty()) {
            SwingUtilities.invokeLater(() -> vistaGrafica.printLine(textoGenerado.trim()));
        }
    }

    @Override 
    public void onGameStateChanged(GameSnapshot snapshot) { 
        this.ultimoSnapshot = snapshot; 
        ejecutarAccionYActualizarUI(() -> consoleUI.onGameStateChanged(snapshot)); 
        if (!bloqueadoPorTransicion) {
            forzarRefrescoVisual(); 
        }
    }

    @Override 
    public void onShipPlaced(Ship ship, int remainingCount) { 
        ejecutarAccionYActualizarUI(() -> consoleUI.onShipPlaced(ship, remainingCount)); 
        forzarRefrescoVisual();
    }

    @Override public void onError(String errorMessage) { ejecutarAccionYActualizarUI(() -> consoleUI.onError(errorMessage)); }
    @Override public void onShotFired(Position pos, ShotResult result, boolean wasPlayer1) { ejecutarAccionYActualizarUI(() -> consoleUI.onShotFired(pos, result, wasPlayer1)); }
}