package com.tpfinal.batallanaval.view;


import javax.swing.JOptionPane;
import javax.swing.JFrame;

  // Componente para volver al Menu Principal.

public class ExitMenuUI {

    public static void backToMainMenu(JFrame actualScreen, MainMenuUI mainMenu) {
        int answer = JOptionPane.showConfirmDialog(
            actualScreen,
            "¿Seguro que querés abandonar la partida actual y volver al menú?",
            "Salir de la partida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (answer == JOptionPane.YES_OPTION) {
            executeMenuTransition(actualScreen, mainMenu);
        }
    }

    // También recibe el menu para el final de la partida
    public static void showGameOver(JFrame actualScreen, String winnerMessage, MainMenuUI mainMenu) {
        String[] options = {"Volver al Menú Principal", "Salir del Juego"};
        
        int choice = JOptionPane.showOptionDialog(
            actualScreen,
            "🎉 ¡PARTIDA TERMINADA! 🎉\n" + winnerMessage + "\n\n¿Qué deseas hacer ahora?",
            "Fin del Juego",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 0) {
            executeMenuTransition(actualScreen, mainMenu);
        } else {
            System.exit(0); 
        }
    }

    
    private static void executeMenuTransition(JFrame actualScreen, MainMenuUI mainMenu) {
        actualScreen.dispose(); // Cierra la partida
        
        if (mainMenu != null) {
            mainMenu.show(); 
        }
    }
}