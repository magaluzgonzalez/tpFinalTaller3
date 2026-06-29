package com.tpfinal.batallanaval.model;

public enum GameState {
    PLACING_SHIPS, // Fase inicial: ambos jugadores colocan sus barcos
    PLAYING,       // Fase de acción: se habilitan los disparos
    FINISHED       // Fin de la partida: alguien ganó
}
