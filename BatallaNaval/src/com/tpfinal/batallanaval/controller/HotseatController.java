package com.tpfinal.batallanaval.controller;

import com.tpfinal.batallanaval.game.Game;
import com.tpfinal.batallanaval.model.*;

public class HotseatController implements PlayerController {
    private final Game game;

    public HotseatController(Game game) {
        this.game = game;
    }

    @Override
    public void attemptPlaceShip(int x, int y, Direction dir) {
        int p1Count = game.getPlayer1().getShips().size();
        int total = game.getConfig().getShipCount();
        
        // Si al J1 le faltan barcos, el comando es para él. Si no, es para el J2.
        Player currentPlayer = (p1Count < total) ? game.getPlayer1() : game.getPlayer2();
        int currentCount = currentPlayer.getShips().size();
        
        if (currentCount < total) {
            int size = game.getConfig().getShipLengths()[currentCount];
            game.placeShip(currentPlayer, new Ship("Barco", size, new Position(x, y), dir));
        }
    }

    @Override
    public void fireShot(int x, int y) {
        // En la fase de disparos no importa quién dispara, el Game usa su propia
        // variable 'isPlayer1Turn' para saber a quién le correspondía este tiro.
        game.processShot(new Position(x, y));
    }
}
