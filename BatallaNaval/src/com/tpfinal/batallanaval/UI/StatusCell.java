package com.tpfinal.batallanaval.UI;

import java.awt.Color;

public enum StatusCell {
    WATER(new Color(30, 144, 255)),     
    SHIP(new Color(255, 215, 0)),       
    IMPACT(new Color(220, 20, 60)),     
    EMPTY(new Color(240, 240, 240));     

    private final Color color;

    StatusCell(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color; 
    }
}
