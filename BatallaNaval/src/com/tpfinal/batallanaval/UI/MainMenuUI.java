package com.tpfinal.batallanaval.UI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tpfinal.batallanaval.game.GameListener;

import java.awt.Component;
import java.awt.Dimension;

public class MainMenuUI extends JFrame {
	private final GameListener listener;
	
	public MainMenuWindow(GameListener listener) {
		super("BattleShip - Menú Principal");
		this.listener = listener;
				
	}
	
}
