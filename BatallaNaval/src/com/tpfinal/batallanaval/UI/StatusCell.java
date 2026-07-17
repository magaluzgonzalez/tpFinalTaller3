package com.tpfinal.batallanaval.UI;

import java.awt.Color;

public enum StatusCell {
    WATER(new Color(30, 144, 255)),       // Azul (Dodger Blue)
    SHIP(new Color(255, 215, 0)),       // Amarillo (Gold)
    IMPACT(new Color(220, 20, 60)),     // Rojo (Crimson)
    EMPTY(new Color(240, 240, 240));     // Gris claro por defecto

    private final Color color;

    StatusCell(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color; 
    }
}
