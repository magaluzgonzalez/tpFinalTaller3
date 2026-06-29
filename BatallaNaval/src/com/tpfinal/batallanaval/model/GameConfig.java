package com.tpfinal.batallanaval.model;

public class GameConfig {
    private final int width;
    private final int height;
    private final int[] shipLengths;
    private final int shipCount;

    public GameConfig(int width, int height, int[] shipLengths) {
        this.width = width;
        this.height = height;
        this.shipLengths = shipLengths;
        this.shipCount = shipLengths.length;
    }

    /**
     * Provides a default configuration (standard 10x10 Battleship).
     */
    public static GameConfig createDefault() {
        return new GameConfig(10, 10, new int[]{2,3,4});
    }

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[] getShipLengths() { return shipLengths; }
    public int getShipCount() { return shipCount; }
}