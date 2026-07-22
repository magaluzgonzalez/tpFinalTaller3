package com.tpfinal.batallanaval.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String name;
    private final int length;
    private final List<Cell> parts;
    private final Direction direction;

    public Ship(String name, int length, Position startPos, Direction dir) {
        this.name = name;
        this.length = length;
        this.direction = dir;
        this.parts = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int x = startPos.getX();
            int y = startPos.getY();

            if (dir == Direction.HORIZONTAL) {
                x += i;
            } else {
                y += i;
            }
            this.parts.add(new Cell(new Position(x, y)));
        }
    }

    /**
     * Replaces 'recibirDisparo'
     */
    public boolean takeHit(Position shotPos) {
        for (Cell part : parts) {
            if (part.getPosition().getX() == shotPos.getX() && 
                part.getPosition().getY() == shotPos.getY()) {
                
                part.setHit(true); // Usamos 'setHit' en lugar de setEstado
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces 'estaHundida'
     */
    public boolean isSunk() {
        for (Cell part : parts) {
            if (!part.isHit()) { // Si alguna parte NO está golpeada
                return false; 
            }
        }
        return true;
    }

    // Getters
    public String getName() { return name; }
    public int getLength() { return length; }
    public List<Cell> getParts() { return parts; }
    public Direction getDirection() { return direction; }
}
