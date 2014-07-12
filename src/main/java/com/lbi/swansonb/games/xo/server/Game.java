package com.lbi.swansonb.games.xo.server;

import javax.websocket.Session;

public class Game {

	Session player1;
	Session player2;

	String turn;

	public Game(Session player1, Session player2) {
		this.player1 = player1;
		this.player2 = player2;

		turn = player1.getId();
	}

	public String handleTurn(Session session) {
		return null;
	}

	public String turnPlayer(){
		return turn;
	}

	public boolean isEnded() {
		return true;
	}

	public boolean isPlayerInGame(String id) {
		String player1 = this.player1.getId();
		String player2 = this.player2.getId();
		return player1.equals(id) || player2.equals(id);
	}
}
