package com.tpfinal.batallanaval.controller;

import com.tpfinal.batallanaval.game.Game;
import com.tpfinal.batallanaval.model.*;

public class LocalController implements PlayerController {
    private final Game game;
    private final boolean isPlayer1;

    public LocalController(Game game, boolean isPlayer1) {
        this.game = game;
        this.isPlayer1 = isPlayer1;
    }

    @Override
    public void attemptPlaceShip(int x, int y, Direction dir) {
        Player me = isPlayer1 ? game.getPlayer1() : game.getPlayer2();
        int myShipCount = me.getShips().size();
        
        if (myShipCount < game.getConfig().getShipCount()) {
            int size = game.getConfig().getShipLengths()[myShipCount];
            game.placeShip(me, new Ship("Barco", size, new Position(x, y), dir));
        }
    }

    @Override
    public void fireShot(int x, int y) {
        game.processShot(new Position(x, y));
    }
}
