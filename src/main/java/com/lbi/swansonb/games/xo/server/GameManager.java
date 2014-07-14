package com.lbi.swansonb.games.xo.server;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameManager {

	private LinkedList<Session> queued;
	private List<Game> games;

	public GameManager() {
		queued = new LinkedList<Session>();
		games = new ArrayList<Game>();
	}

	public LinkedList<Session> getQueued() {
		return queued;
	}

	public List<Game> getGames() {
		return games;
	}
}
