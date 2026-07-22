package com.tpfinal.batallanaval.view;

import com.tpfinal.batallanaval.UI.PerspectiveManager;
import com.tpfinal.batallanaval.controller.PlayerController;
import com.tpfinal.batallanaval.game.GameListener;
import com.tpfinal.batallanaval.model.*;
import javax.swing.Timer;
import javax.swing.SwingUtilities;


 // Adaptador principal de interfaz grafica que implementa la interfaz GameListener.
 // Gestiona las interacciones de entrada del mouse, coordina el refresco de las vistas
 // y controla los tiempos de transicion entre turnos.
 
public class GraphicUI implements GameListener {
    
    private final PlayerController controller; 
    private final GameConfig config;
    private final Boolean fixedPerspectiveP1;
    private final PerspectiveManager perspectiveManager;
    
    private final SwingUI graphicView; 
    private final ConsoleUI consoleUI; 
    private GameSnapshot lastSnapshot;
    private boolean blockedByTransition = false;
    
    private final MainMenuUI mainMenu; 

    
     // Construye la fachada grafica e inicializa los componentes de vista y bitacora.
      
    /* @param controller = controlador encendido para enviar las ordenes del jugador.
     * @param config = configuracion de parametros globales de la partida.
     * @param fixedPerspectiveP1 = define si la perspectiva es fija (Red/IA) o rotativa (PvP).
     * @param mainMenu = instancia del menu principal para retornar al finalizar la partida.
     */
    
    public GraphicUI(PlayerController controller, GameConfig config, Boolean fixedPerspectiveP1, MainMenuUI mainMenu) {
        this.controller = controller;
        this.config = config;
        this.fixedPerspectiveP1 = fixedPerspectiveP1;
        this.mainMenu = mainMenu; 

        this.graphicView = new SwingUI("Battleship - Control", config.getWidth(), config.getHeight(), this::mapMouseClick, mainMenu);
        this.perspectiveManager = new PerspectiveManager(config, fixedPerspectiveP1);
        this.consoleUI = new ConsoleUI(controller, config, fixedPerspectiveP1, this.graphicView);
    }

    
     // Captura y valida los eventos de clic provenientes del tablero grafico.
     // Mapea las coordenadas (X, Y) y delega la ejecucion de la jugada al controlador.
    
    private void mapMouseClick(int x, int y, boolean isMyFleet, boolean isLeftClick) {
        if (lastSnapshot == null || blockedByTransition) return;

        // FASE 1: Colocacion embarcaciones
        if (lastSnapshot.state == GameState.PLACING_SHIPS) {
            boolean itsP1turnToPlace = (fixedPerspectiveP1 != null) ? fixedPerspectiveP1 : 
                (lastSnapshot.player1Ships.size() < config.getShipCount());

            if ((itsP1turnToPlace && isMyFleet) || (!itsP1turnToPlace && !isMyFleet)) {
                Direction dir = isLeftClick ? Direction.HORIZONTAL : Direction.VERTICAL;
                controller.attemptPlaceShip(x, y, dir);
                forceVisualRefresh();
            } else {
                graphicView.printLine("❌ No podés colocar barcos en el tablero del rival.");
            }
            return;
        }
        
        // FASE 2: Desarrollo de la fase de disparos
        if (lastSnapshot.state == GameState.PLAYING && isLeftClick) {
            if (fixedPerspectiveP1 != null) {
                // Validacion para modos Red / IA
                boolean isMyTurnRed = (fixedPerspectiveP1 && lastSnapshot.isPlayer1Turn) || (!fixedPerspectiveP1 && !lastSnapshot.isPlayer1Turn);
                boolean clickRadarRed = (fixedPerspectiveP1 && !isMyFleet) || (!fixedPerspectiveP1 && isMyFleet);

                if (isMyTurnRed) {
                    if (clickRadarRed) {
                        controller.fireShot(x, y);
                        forceVisualRefresh();
                    } else {
                        graphicView.printLine("❌ ¡Disparo inválido! No podés dispararle a tu propia flota.");
                    }
                } else {
                    graphicView.printLine("⚠️ Esperá, es el turno del enemigo remoto.");
                }
            } else {
                // Validacion para modo Local Hotseat (PvP)
                boolean validateClickLocal = (lastSnapshot.isPlayer1Turn && !isMyFleet) || (!lastSnapshot.isPlayer1Turn && isMyFleet);

                if (validateClickLocal) {
                    controller.fireShot(x, y);
                } else {
                    graphicView.printLine("❌ ¡Disparo inválido! No podés dispararle a tu propia flota.");
                }
            }
        }
    }

    
     // Forzar la actualizacion visual de las matrices en la pantalla Swing
     // invocando al gestor de perspectiva en el hilo grafico de Swing.
     
    private void forceVisualRefresh() {
        if (lastSnapshot != null) {
            SwingUtilities.invokeLater(() -> perspectiveManager.applyPerspective(lastSnapshot, graphicView));
        }
    }

    // --- MÉTODOS DEL OBSERVER (GameListener) ---

    @Override
    public void onGameStateChanged(GameSnapshot snapshot) {
        this.lastSnapshot = snapshot;
        this.consoleUI.onGameStateChanged(snapshot);

        // Disparo de modal de fin de partida al detectar el estado FINISHED
        if (snapshot.state == GameState.FINISHED) {
            String winner = snapshot.isPlayer1Turn ? "Ganador: JUGADOR 1" : "Ganador: JUGADOR 2";
            
            SwingUtilities.invokeLater(() -> {
               OptionManager.showGameOver(graphicView, winner, mainMenu);
            });
            return;
        }

        if (!blockedByTransition) {
            forceVisualRefresh();
        }
    }

    @Override
    public void onShipPlaced(Ship ship, int remainingCount) {
        this.consoleUI.onShipPlaced(ship, remainingCount);
    }

    
     // Procesa los eventos de disparo lanzados por el motor.
     // En modo PvP, si el disparo resulta en AGUA, congela temporalmente la pantalla 
     // mediante un Swing Timer para permitir el cambio de jugador.
     
    @Override
    public void onShotFired(Position pos, ShotResult result, boolean isPlayer1Turn) {
        this.consoleUI.onShotFired(pos, result, isPlayer1Turn);

        if (lastSnapshot != null && lastSnapshot.state == GameState.FINISHED) {
            return;
        }

        // Gestión del delay de transición en modo Hotseat Local
        if (fixedPerspectiveP1 == null) {
            if (result == ShotResult.MISS) {
                final boolean turnOfTheWhoFired = lastSnapshot.isPlayer1Turn;

                // Mantiene fija la pantalla con el turno del tirador para mostrar el impacto en agua
                SwingUtilities.invokeLater(() -> {
                    perspectiveManager.applyPerspectiveWithFixedTurn(lastSnapshot, graphicView, turnOfTheWhoFired);
                });

                blockedByTransition = true;
                graphicView.printLine("\n=========================================");
                graphicView.printLine("💦 ¡AGUA! CAMBIANDO DE TURNO...");
                graphicView.printLine("Por favor, cedele la silla al siguiente jugador.");
                graphicView.printLine("=========================================\n");

                // Temporizador asincrónico para desbloquear la entrada y rotar la pantalla
                Timer timer = new Timer(2000, e -> {
                    blockedByTransition = false; 
                    forceVisualRefresh(); 
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                forceVisualRefresh();
            }
        } 
        else {
            forceVisualRefresh();
        }
    }

    
     // Captura excepciones del motor o señales de interrupcion de red.
     
    @Override
    public void onError(String errorMessage) {
        if (this.consoleUI != null) {
            this.consoleUI.onError(errorMessage);
        }
        
        // Manejo de desconexión de Sockets
        if (errorMessage != null && errorMessage.startsWith("DESCONEXION:")) {
            String cleanMessage = errorMessage.substring(12).trim(); 
            
            SwingUtilities.invokeLater(() -> {
                OptionManager.showGameOver(
                    this.graphicView, 
                    "❌ " + cleanMessage, 
                    this.mainMenu
                );
            });
        }
    }
}
