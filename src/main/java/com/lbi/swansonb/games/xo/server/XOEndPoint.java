package com.lbi.swansonb.games.xo.server;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ServerEndpoint(value = "/xo")
public class XOEndPoint {

	private static final String ERROR = "Unable to process turn.";
	private static final String YOU_ARE_NOW_IN_A_GAME = "You are now in a game.";
	private static final String NOT_ENOUGH_PLAYERS = "Not enough players. You are in a queue.";
	LinkedList<Session> queued;
	List<Game> games;

	public XOEndPoint(){
		queued = new LinkedList<Session>();
		games = new ArrayList<Game>(games);
	}

	@OnOpen
	public String onOpen(Session session){
		cleanupGames();

		if(queued.size() > 0){
			Session player1 = session;
			Session player2 = queued.pop();
			games.add(new Game(player1, player2));
			return YOU_ARE_NOW_IN_A_GAME;
		} else {
			queued.add(session);
			return NOT_ENOUGH_PLAYERS;
		}
	}

	private void cleanupGames() {
		Iterator<Game> iterator = games.iterator();
		while(iterator.hasNext()){
			Game game = iterator.next();
			if(game.isGameDead()){
				iterator.remove();
			}
		}
	}

	@OnMessage
    public String onMessage(String message, Session session) {
		String id = session.getId();
		Game game = getGame(id);

		if(isGaming(game)){
			return game.handleTurn(message, session);
		} else if(isQueued(id)) {
			return NOT_ENOUGH_PLAYERS;
		} else {
			return ERROR;
		}
    }

	public boolean isQueued(String id) {
		return queued.contains(id);
	}

	public boolean isGaming(Game game) {
		return game == null;
	}

	public Game getGame(String id) {
		for(Game game : games) {
			if(game.isPlayerInGame(id)){
				return game;
			}
		}

		return null;
	}
}