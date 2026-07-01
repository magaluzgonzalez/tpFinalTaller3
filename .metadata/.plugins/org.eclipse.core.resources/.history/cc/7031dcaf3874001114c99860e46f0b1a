package com.tpfinal.batallanaval.network;

import com.tpfinal.batallanaval.controller.PlayerController;
import com.tpfinal.batallanaval.game.GameListener;
import com.tpfinal.batallanaval.model.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClientController implements PlayerController {
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private GameListener ui; // Quien sea que dibuje la pantalla (Consola o Gráfica)

    public NetworkClientController(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("[RED] ¡Conectado al Host exitosamente!");
        } catch (Exception e) {
            System.out.println("[RED] No se pudo conectar al Host en " + ip + ":" + port);
            System.exit(1); // Terminamos si no hay conexión
        }
    }

    // Le pasamos la UI para que el hilo de red sepa a quién avisarle cuando llegan datos
    public void setUI(GameListener ui) {
        this.ui = ui;
        iniciarEscucha();
    }

    private void iniciarEscucha() {
        new Thread(() -> {
            try {
                while (true) {
                    Object recibido = entrada.readObject();
                    
                    if (recibido instanceof GameSnapshot && ui != null) {
                        ui.onGameStateChanged((GameSnapshot) recibido);
                    } 
                    else if (recibido instanceof String && ui != null) {
                        String msg = (String) recibido;
                        if (msg.startsWith("ERROR:")) {
                            ui.onError(msg.substring(6)); // Borramos la palabra "ERROR:"
                        } else {
                            System.out.println("\n[MENSAJE DEL SERVIDOR]: " + msg);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("\n[RED] El Host cerró la partida.");
                System.exit(0);
            }
        }).start();
    }

    // --- MÉTODOS DEL PLAYERCONTROLLER (Enviamos a la red en vez del juego) ---

    @Override
    public void attemptPlaceShip(int x, int y, Direction dir) {
        try {
            salida.writeObject("PLACE " + x + " " + y + " " + dir.name());
        } catch (Exception e) {}
    }

    @Override
    public void fireShot(int x, int y) {
        try {
            salida.writeObject("SHOT " + x + " " + y);
        } catch (Exception e) {}
    }
}
