package com.tpfinal.batallanaval.game;

import com.tpfinal.batallanaval.model.GameSnapshot;
import com.tpfinal.batallanaval.model.Position;
import com.tpfinal.batallanaval.model.Ship;
import com.tpfinal.batallanaval.model.ShotResult;

public interface GameListener {
    // El renderizado general
    void onGameStateChanged(GameSnapshot snapshot);

    // --- NUEVOS EVENTOS ESPECÍFICOS ---
    
    // Al colocar barcos
    void onShipPlaced(Ship ship, int remainingCount);
    void onError(String errorMessage);

    // Al disparar
    void onShotFired(Position pos, ShotResult result, boolean isPlayer1Turn);
}
