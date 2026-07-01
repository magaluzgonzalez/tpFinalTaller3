package com.tpfinal.batallanaval.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class SwingUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea terminalOutput;
    private JTextField commandInput;
    private Consumer<String> onCommandSubmitted;

    private BoardPanel panelMiFlota;
    private BoardPanel panelRadar;
    
    // Guardamos los labels de los títulos para cambiarlos dinámicamente
    private JLabel lblTituloFlota;
    private JLabel lblTituloRadar;

    public SwingUI(String titulo, int columnas, int filas) {
        setTitle(titulo);
        initUI(columnas, filas);
    } 

    private void initUI(int columnas, int filas) {
        setSize(900, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(10, 25, 47));

        terminalOutput = new JTextArea();
        terminalOutput.setEditable(false);
        terminalOutput.setBackground(Color.WHITE); 
        terminalOutput.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
        terminalOutput.setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(terminalOutput);
        scrollPane.setPreferredSize(new Dimension(850, 250)); 
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelContenedorTableros = new JPanel(new GridLayout(1, 2, 20, 0));
        
        panelMiFlota = new BoardPanel(columnas, filas, false, null);
        panelRadar = new BoardPanel(columnas, filas, false, null); 

        // Inicializamos los labels correspondientes
        lblTituloFlota = new JLabel("", SwingConstants.CENTER);
        lblTituloRadar = new JLabel("", SwingConstants.CENTER);

        panelContenedorTableros.add(crearContenedorConTitulo(lblTituloFlota, panelMiFlota));
        panelContenedorTableros.add(crearContenedorConTitulo(lblTituloRadar, panelRadar));
        mainPanel.add(panelContenedorTableros, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblInput = new JLabel("Ingresar comando: ");
        commandInput = new JTextField();
        commandInput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        commandInput.addActionListener(e -> {
            String input = commandInput.getText().trim();
            commandInput.setText("");
            if (onCommandSubmitted != null && !input.isEmpty()) {
                onCommandSubmitted.accept(input);
            }
        });

        inputPanel.add(lblInput, BorderLayout.WEST);
        inputPanel.add(commandInput, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        commandInput.requestFocusInWindow();
    }

    // Refactorizado para usar los labels dinámicos
    private JPanel crearContenedorConTitulo(JLabel labelTitulo, BoardPanel tablero) {
        JPanel contenedor = new JPanel(new BorderLayout(5, 5));
        Color azulOscuro = new Color(10, 25, 47);
        
        contenedor.setOpaque(true);
        contenedor.setBackground(azulOscuro);
        
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelTitulo.setForeground(Color.WHITE); 
        
        contenedor.add(labelTitulo, BorderLayout.NORTH);
        contenedor.add(tablero, BorderLayout.CENTER);
        
        return contenedor;
    }

    // Nuevo método para cambiar los textos del encabezado en cada turno
    public void actualizarTitulos(String tituloFlota, String tituloRadar) {
        lblTituloFlota.setText(tituloFlota);
        lblTituloRadar.setText(tituloRadar);
    }

    public void setOnCommandSubmitted(Consumer<String> callback) { this.onCommandSubmitted = callback; }
    public void limpiarPantalla() { terminalOutput.setText(""); }
    public void printLine(String text) { terminalOutput.append(text + "\n"); }
    public void actualizarCasilleroMiFlota(int x, int y, StatusCell status) { panelMiFlota.actualizarCasillero(x, y, status); }
    public void actualizarCasilleroRadar(int x, int y, StatusCell status) { panelRadar.actualizarCasillero(x, y, status); }
}