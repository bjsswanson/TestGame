package com.lbi.swansonb.games.xo.server;

import org.glassfish.tyrus.server.Server;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ServerEndpoint(value = "/xo")
public class XOServer {

	private static final String ERROR = "Unable to process turn.";
	private static final String YOU_ARE_NOW_IN_A_GAME = "You are now in a game.";
	private static final String NOT_ENOUGH_PLAYERS = "Not enough players. You are in a queue.";
	private static final String PLAYER_CONNECTED = "Player Connected.";
	LinkedList<Session> queued;
	List<Game> games;

	public XOServer(){
		queued = new LinkedList<Session>();
		games = new ArrayList<Game>(games);
	}

	@OnOpen
	public String onOpen(Session session){
		System.out.println(PLAYER_CONNECTED);
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

	@OnMessage
    public String onMessage(String message, Session session) {
		System.out.println("Recieved: " + message);

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

	private void cleanupGames() {
		Iterator<Game> iterator = games.iterator();
		while(iterator.hasNext()){
			Game game = iterator.next();
			if(game.isGameDead()){
				iterator.remove();
			}
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

	public static void main(String[] args){
		Server server = new Server("localhost", 8025, "/websocket", null, XOServer.class);
	    try {
	        server.start();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        System.out.print("Please press a key to stop the server.");
	        reader.readLine();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        server.stop();
	    }
	}
}