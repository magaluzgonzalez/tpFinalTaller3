package com.tpfinal.batallanaval.model;

import java.io.Serializable;
import java.util.List;

/**
 * Una "fotografía" inmutable del estado actual del juego.
 * Solo contiene la información que la UI necesita dibujar.
 */
public class GameSnapshot implements Serializable {
    public final GameState state;
    public final boolean isPlayer1Turn;
    
    // Lo que vemos del Jugador 1
    public final List<Ship> player1Ships; 
    public final List<Position> player1MissedShots;

    // Lo que vemos del Jugador 2 (¡Ocultamos sus barcos sanos si somos el P1!)
    // Para simplificar ahora, pasamos las listas completas, pero en el futuro
    // aquí solo pasaríamos las partes "isHit = true".
    public final List<Ship> player2Ships; 
    public final List<Position> player2MissedShots;

    public GameSnapshot(GameState state, boolean isPlayer1Turn, 
                        List<Ship> p1Ships, List<Position> p1Misses,
                        List<Ship> p2Ships, List<Position> p2Misses) {
        this.state = state;
        this.isPlayer1Turn = isPlayer1Turn;
        this.player1Ships = p1Ships;
        this.player1MissedShots = p1Misses;
        this.player2Ships = p2Ships;
        this.player2MissedShots = p2Misses;
    }
}
