package com.tpfinal.batallanaval.view;

import com.tpfinal.batallanaval.controller.LocalController;
import com.tpfinal.batallanaval.game.GameListener;
import com.tpfinal.batallanaval.model.Direction;
import com.tpfinal.batallanaval.model.GameSnapshot;
import com.tpfinal.batallanaval.model.Position;
import com.tpfinal.batallanaval.model.Ship;
import com.tpfinal.batallanaval.model.ShotResult;

public class GraphicUI implements GameListener {
    private LocalController controller;

    // Tu compañero recibe el controlador cuando construyen su interfaz
    public GraphicUI(LocalController controller) {
        this.controller = controller;
        //construirBotones(); // Su código de Java Swing / FX
    }

    // --- CÓMO ENVÍA INPUTS ---
    
    // Su botón de la grilla de colocación
    private void alHacerClicParaColocar(int gridX, int gridY, Direction dirSeleccionada) {
        controller.attemptPlaceShip(gridX, gridY, dirSeleccionada);
    }

    // Su botón del radar de ataque
    private void alHacerClicParaAtacar(int gridX, int gridY) {
        controller.fireShot(gridX, gridY);
    }

	@Override
	public void onGameStateChanged(GameSnapshot snapshot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShipPlaced(Ship ship, int remainingCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShotFired(Position pos, ShotResult result, boolean isPlayer1Turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String errorMessage) {
		// TODO Auto-generated method stub
		
	}
}