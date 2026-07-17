package com.tpfinal.batallanaval.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class BoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int columnas;
    private final int filas;
    private final CheckboxButton[][] matriz;

    // Ahora recibimos un callback especial que también nos dice si fue Clic Izquierdo (true) o Derecho (false)
    public BoardPanel(int columnas, int filas, boolean esInteractivo, BiConsumer<CheckboxButton, Boolean> onButtonMouseClick) {
        this.columnas = columnas;
        this.filas = filas;
        this.matriz = new CheckboxButton[columnas][filas];

        setLayout(new GridLayout(filas, columnas, 2, 2));

        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                CheckboxButton boton = new CheckboxButton(x, y);
                matriz[x][y] = boton;

                if (esInteractivo && onButtonMouseClick != null) {
                    // Escuchamos los eventos del mouse de forma detallada
                    boton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (!boton.isEnabled()) return;
                            
                            // e.getButton() == 1 es Clic Izquierdo (Horizontal / Disparar)
                            // e.getButton() == 3 es Clic Derecho (Vertical)
                            boolean esClicIzquierdo = (e.getButton() == MouseEvent.BUTTON1);
                            onButtonMouseClick.accept(boton, esClicIzquierdo);
                        }
                    });
                }
                add(boton);
            }
        }
    }

    public void actualizarCasillero(int x, int y, StatusCell status) {
        if (x >= 0 && x < columnas && y >= 0 && y < filas) {
            matriz[x][y].setEstado(status);
        }
    }
}