package com.tpfinal.batallanaval.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class BoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int columns;
    private final int rows;
    private final CheckboxButton[][] matrix;

    public BoardPanel(int columns, int rows, boolean isInteractive, BiConsumer<CheckboxButton, Boolean> onButtonMouseClick) {
        this.columns = columns;
        this.rows = rows;
        this.matrix = new CheckboxButton[columns][rows];

        // 🛠️ MODIFICADO: Le sumamos 1 a las filas y 1 a las columnas para el marco de números
        setLayout(new GridLayout(rows + 1, columns + 1, 2, 2));
        setBackground(new Color(10, 25, 47)); // Fondo oscuro a tono con tu UI

        // =========================================================================
        // 1. FILA DE ARRIBA: Esquina vacía + Números de las Columnas (0 al 9)
        // =========================================================================
        add(new JLabel("")); // El casillero vacío de la esquina superior izquierda
        
        for (int x = 0; x < columns; x++) {
            JLabel lblCol = new JLabel(String.valueOf(x), SwingConstants.CENTER);
            lblCol.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblCol.setForeground(Color.LIGHT_GRAY);
            add(lblCol);
        }

        // =========================================================================
        // 2. CUERPO DEL TABLERO: Número de fila en la izquierda + Botones de la matriz
        // =========================================================================
        for (int y = 0; y < rows; y++) {
            
            // Ponemos el número de la fila actual antes de meter sus botones
            JLabel lblRow = new JLabel(String.valueOf(y), SwingConstants.CENTER);
            lblRow.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblRow.setForeground(Color.LIGHT_GRAY);
            add(lblRow);

            for (int x = 0; x < columns; x++) {
                CheckboxButton button = new CheckboxButton(x, y);
                matrix[x][y] = button;

                if (isInteractive && onButtonMouseClick != null) {
                	button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (!button.isEnabled()) return;
                            
                            boolean isLeftClick = (e.getButton() == MouseEvent.BUTTON1);
                            onButtonMouseClick.accept(button, isLeftClick);
                        }
                    });
                }
                add(button); // Se agrega el botón normalmente
            }
        }
    }

    public void updateCell(int x, int y, StatusCell status) {
        // Tu lógica de actualización sigue intacta porque la matriz interna no cambió de tamaño
        if (x >= 0 && x < columns && y >= 0 && y < rows) {
        	matrix[x][y].setStatus(status);
        }
        
    }
 }