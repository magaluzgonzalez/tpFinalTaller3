package com.tpfinal.batallanaval.UI;

import java.awt.Color;

public enum StatusCell {
    AGUA(new Color(30, 144, 255)),       // Azul (Dodger Blue)
    BARCO(new Color(255, 215, 0)),       // Amarillo (Gold)
    IMPACTO(new Color(220, 20, 60)),     // Rojo (Crimson)
    VACIO(new Color(240, 240, 240));     // Gris claro por defecto

    private final Color color;

    StatusCell(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color; 
    }
}
