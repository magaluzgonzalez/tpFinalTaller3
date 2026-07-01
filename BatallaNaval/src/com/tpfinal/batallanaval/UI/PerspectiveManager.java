package com.tpfinal.batallanaval.UI;

import com.tpfinal.batallanaval.model.*;
import java.util.List;

public class PerspectiveManager {
    private final GameConfig config;
    private final Boolean fixedPerspectiveP1;

    public PerspectiveManager(GameConfig config, Boolean fixedPerspectiveP1) {
        this.config = config;
        this.fixedPerspectiveP1 = fixedPerspectiveP1;
    }

    public void aplicarPerspectiva(GameSnapshot snapshot, SwingUI vista) {
        // 1. Averiguamos de quién es el turno actual
        boolean esTurnoP1;
        if (fixedPerspectiveP1 != null) {
            esTurnoP1 = fixedPerspectiveP1; // IA o Red (Fijo)
        } else {
            if (snapshot.state == GameState.PLACING_SHIPS) {
                // En colocación, depende de a quién le falten barcos
                esTurnoP1 = (snapshot.player1Ships.size() < config.getShipCount());
            } else {
                // Jugando, depende del turno del motor
                esTurnoP1 = snapshot.isPlayer1Turn;
            }
        }

        // 2. Actualizamos los títulos superiores de la UI para dar contexto claro
        String fase = (snapshot.state == GameState.PLACING_SHIPS) ? "Fase: COLOCACIÓN" : "Fase: JUGANDO";
        String jugadorActivo = esTurnoP1 ? "TURNO: JUGADOR 1" : "TURNO: JUGADOR 2";
        vista.actualizarTitulos(fase, jugadorActivo);

        // 3. Recorremos las grillas. 
        // IMPORTANTE: Izquierda es SIEMPRE J1, Derecha es SIEMPRE J2.
        for (int y = 0; y < config.getHeight(); y++) {
            for (int x = 0; x < config.getWidth(); x++) {
                Position pos = new Position(x, y);

                // =============================================================
                // TABLERO IZQUIERDO: Datos del JUGADOR 1
                // =============================================================
                StatusCell estadoJ1 = snapshot.player1MissedShots.contains(pos) ? StatusCell.AGUA : StatusCell.VACIO;
                for (Ship s : snapshot.player1Ships) {
                    for (Cell c : s.getParts()) {
                        if (c.getPosition().equals(pos)) {
                            if (c.isHit()) {
                                estadoJ1 = StatusCell.IMPACTO; // Recibió daño (Rojo)
                            } else {
                                // REGLA OCULTACIÓN: Solo el J1 puede ver sus barcos intactos en su turno.
                                // Si es el turno del J2, para el J2 este es su tablero de ataque, ve todo vacío (~ OCULTO)
                                estadoJ1 = esTurnoP1 ? StatusCell.BARCO : StatusCell.VACIO; 
                            }
                        }
                    }
                }
                vista.actualizarCasilleroMiFlota(x, y, estadoJ1);

                // =============================================================
                // TABLERO DERECHO: Datos del JUGADOR 2
                // =============================================================
                StatusCell estadoJ2 = snapshot.player2MissedShots.contains(pos) ? StatusCell.AGUA : StatusCell.VACIO;
                for (Ship s : snapshot.player2Ships) {
                    for (Cell c : s.getParts()) {
                        if (c.getPosition().equals(pos)) {
                            if (c.isHit()) {
                                estadoJ2 = StatusCell.IMPACTO; // Recibió daño (Rojo)
                            } else {
                                // REGLA OCULTACIÓN: Solo el J2 puede ver sus barcos intactos en su turno.
                                // Si es el turno del J1, para el J1 este es su radar de ataque, ve todo vacío (~ OCULTO)
                                estadoJ2 = !esTurnoP1 ? StatusCell.BARCO : StatusCell.VACIO; 
                            }
                        }
                    }
                }
                vista.actualizarCasilleroRadar(x, y, estadoJ2);
            }
        }
    }
}