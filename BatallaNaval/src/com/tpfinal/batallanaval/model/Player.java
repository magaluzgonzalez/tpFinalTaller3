package com.tpfinal.batallanaval.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Ship> ships;
    private final List<Position> missedShots; // Para recordar dónde tiró y fue "Agua"

    public Player(String name) {
        this.name = name;
        this.ships = new ArrayList<>();
        this.missedShots = new ArrayList<>();
    }

    // --- LÓGICA DE RECIBIR UN TIRO ---
    
    /**
     * El jugador recibe un tiro del oponente.
     * @return el resultado del tiro (ShotResult)
     */
    public ShotResult receiveShot(Position shotPos) {
        for (Ship ship : ships) {
            if (ship.takeHit(shotPos)) {
                // Le dimos a un barco. ¿Se hundió con este tiro?
                if (ship.isSunk()) {
                    return ShotResult.SUNK;
                }
                return ShotResult.HIT;
            }
        }
        
        // Si llegamos aquí, no le dimos a nada
        missedShots.add(shotPos);
        return ShotResult.MISS;
    }

    /**
     * Verifica si el jugador perdió la partida.
     */
    public boolean hasLost() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false; // Al menos un barco sigue vivo
            }
        }
        return true; // Todos hundidos
    }
    
    /**
     * Verifica si el jugador ya tiene una parte de un barco en esta posición.
     */
    public boolean hasShipAt(Position pos) {
        for (Ship ship : ships) {
            for (Cell part : ship.getParts()) {
                // Aquí entra en juego el equals() que le agregamos a Position
                if (part.getPosition().equals(pos)) {
                    return true; // ¡Ya hay un barco aquí!
                }
            }
        }
        return false; // Está libre
    }
    
    public boolean hasAlreadyBeenShot(Position pos) {
        // 1. Ya disparamos ahí y fue agua?
        if (missedShots.contains(pos)) return true;
        
        // 2. Ya disparamos ahí y le dimos a un barco?
        for (Ship ship : ships) {
            for (Cell part : ship.getParts()) {
                if (part.getPosition().equals(pos) && part.isHit()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Getters y métodos para agregar barcos (placeShip)...
    public void addShip(Ship ship) { this.ships.add(ship); }
    public String getName() { return name; }
    public List<Ship> getShips() { return ships; }
    public List<Position> getMissedShots() { return missedShots; }
}