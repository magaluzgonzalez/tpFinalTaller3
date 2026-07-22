package com.tpfinal.batallanaval.view;

import javax.swing.*;

import com.tpfinal.batallanaval.UI.BoardPanel;
import com.tpfinal.batallanaval.UI.StatusCell;

import java.awt.*;

 // Ventana principal de interfaz grafica para el desarrollo de la partida.
 // Encapsula la maqueta visual compuesta por ambos tableros de juego (Mi Flota y Radar),
 // la cabecera de control de sesion y la consola/bitácora de texto en tiempo real.

public class SwingUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea terminalOutput;
    
    private BoardPanel panelMyFleet;
    private BoardPanel panelRadar; 
    private JLabel lblMyFleetTitle;
    private JLabel lblRadarTitle;

   
     // Construye e inicializa el marco de la ventana gráfica principal.
     /* 
     * @param title = titulo visible en la barra superior de la ventana.
     * @param clickDelegate = delegado funcional para la captura de eventos de mouse.
     * @param mainmenu = referencia al Menu Principal para posibilitar el retorno.
     */
    public SwingUI(String title, int columns, int rows, MouseClickDelegate clickDelegate, MainMenuUI mainmenu) {
        setTitle(title);
        initUI(columns, rows, clickDelegate, mainmenu);
    } 

     // Ensamblor de paneles organizandolos con BorderLaout y GridLayout.
     
    private void initUI(int columns, int rows, MouseClickDelegate clickDelegate, MainMenuUI mainmenu) {
        setSize(900, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(10, 25, 47));

        //Cabecera Norte: retorno al menu principal
        JButton backBtn = new JButton("🚪 Abandonar Partida y Volver al Menú");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        backBtn.setBackground(new Color(200, 50, 50)); 
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);

        backBtn.addActionListener(e -> {
            OptionManager.backToMainMenu(this, mainmenu);
        });

        mainPanel.add(backBtn, BorderLayout.NORTH);

        //Zona Sur: Terminal de texto en formato JTextArea dentro de un JScrollPane
        terminalOutput = new JTextArea();
        terminalOutput.setEditable(false);  
        terminalOutput.setBackground(Color.WHITE); 
        terminalOutput.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
        terminalOutput.setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(terminalOutput);
        scrollPane.setPreferredSize(new Dimension(850, 200)); 
        mainPanel.add(scrollPane, BorderLayout.SOUTH); 

        //Zona Central: Contenedor doble de tableros organizados en 1 fila y 2 columnas
        JPanel dashboardContainerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        panelMyFleet = new BoardPanel(columns, rows, true, (btn, isLeft) -> {
            clickDelegate.onGridClicked(btn.getXCoord(), btn.getYCoord(), true, isLeft);
        });

        panelRadar = new BoardPanel(columns, rows, true, (btn, isLeft) -> {
            clickDelegate.onGridClicked(btn.getXCoord(), btn.getYCoord(), false, isLeft);
        }); 

        lblMyFleetTitle = new JLabel("", SwingConstants.CENTER);
        lblRadarTitle = new JLabel("", SwingConstants.CENTER);

        dashboardContainerPanel.add(createContainerWithTitle(lblMyFleetTitle, panelMyFleet));
        dashboardContainerPanel.add(createContainerWithTitle(lblRadarTitle, panelRadar));
        
        mainPanel.add(dashboardContainerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    } 

    
     // Metodo auxiliar para empaquetar un tablero individual con su respectiva etiqueta de titulo.
    
    private JPanel createContainerWithTitle(JLabel labelTitle, BoardPanel board) {
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setOpaque(true);
        container.setBackground(new Color(10, 25, 47));
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelTitle.setForeground(Color.WHITE); 
        container.add(labelTitle, BorderLayout.NORTH);
        container.add(board, BorderLayout.CENTER);
        return container;
    }

    // Modifica el texto informativo de los títulos superiores de ambos tableros.
     
    public void updateTitles(String titleFleet, String titleRadar) {
        lblMyFleetTitle.setText(titleFleet);
        lblRadarTitle.setText(titleRadar);
    }

    
    // Limpia todo el contenido del área de texto de la bitácora.
     
    public void clearScreen() { 
        terminalOutput.setText(""); 
    }

    // Imprime una línea de texto en la bitácora desplazando automáticamente el scroll hacia el final.
     
    public void printLine(String text) { 
        terminalOutput.append(text + "\n"); 
        terminalOutput.setCaretPosition(terminalOutput.getDocument().getLength());
    }
    
    // Actualiza una celda específica del tablero de la flota propia.
     
    public void updateMyFleetCell(int x, int y, StatusCell status) { 
        panelMyFleet.updateCell(x, y, status); 
    }

    // Actualiza una celda específica del tablero de radar/ataque.
    
    public void updateRadarGrid(int x, int y, StatusCell status) { 
        panelRadar.updateCell(x, y, status); 
    }

    
    //Interfaz funcional interna para delegar la recepción de clics hacia los adaptadores.
     
    @FunctionalInterface
    public interface MouseClickDelegate {
        void onGridClicked(int x, int y, boolean isMyFleet, boolean isLeftClick);
    }
}