package com.tpfinal.batallanaval.network;

import com.tpfinal.batallanaval.game.*;
import com.tpfinal.batallanaval.model.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer {
    private final Game game;
    private final int port;

    public NetworkServer(Game game, int port) {
        this.game = game;
        this.port = port;
    }

    public void startListeningInBackground() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("\n[RED] Esperando a que el Jugador 2 se conecte (Puerto " + port + ")...");
                Socket socket = serverSocket.accept();
                System.out.println("[RED] ¡Jugador 2 conectado desde " + socket.getInetAddress() + "!");

                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

                // 1. EL MEGÁFONO: Traduce eventos del juego a la red
                game.addListener(new GameListener() {
                    @Override
                    public void onGameStateChanged(GameSnapshot snapshot) {
                        try {
                            // Bloque synchronized: Evita que dos hilos choquen al enviar datos
                            synchronized (salida) {
                                salida.writeObject(snapshot);
                                salida.reset(); // Limpiamos la caché
                            }
                        } catch (Exception e) {}
                    }

                    @Override
                    public void onError(String errorMessage) {
                        try {
                            synchronized (salida) {
                                salida.writeObject("ERROR:" + errorMessage);
                            }
                        } catch (Exception e) {}
                    }
                    
                    @Override public void onShipPlaced(Ship ship, int remaining) {}
                    @Override public void onShotFired(Position pos, ShotResult result, boolean p1Turn) {}
                });

                // Forzamos el envío de la foto actual al cliente recién conectado
                game.notifyListeners();

                // 2. EL OÍDO: Traduce comandos de la red al juego
                while (true) {
                    String comando = (String) entrada.readObject();
                    
                    if (comando.startsWith("PLACE")) {
                        String[] partes = comando.split(" ");
                        int x = Integer.parseInt(partes[1]);
                        int y = Integer.parseInt(partes[2]);
                        Direction dir = partes[3].equals("HORIZONTAL") ? Direction.HORIZONTAL : Direction.VERTICAL;
                        
                        Player p2 = game.getPlayer2();
                        if (p2.getShips().size() < game.getConfig().getShipCount()) {
                            int size = game.getConfig().getShipLengths()[p2.getShips().size()];
                            game.placeShip(p2, new Ship("Barco", size, new Position(x, y), dir));
                        }
                    } 
                    else if (comando.startsWith("SHOT")) {
                        String[] partes = comando.split(" ");
                        int x = Integer.parseInt(partes[1]);
                        int y = Integer.parseInt(partes[2]);
                        game.processShot(new Position(x, y));
                    }
                }

            } catch (Exception e) {
                System.out.println("\n[RED] Conexión con el cliente perdida.");
            }
        }).start(); // ¡Inicia el hilo secundario!
    }
}
