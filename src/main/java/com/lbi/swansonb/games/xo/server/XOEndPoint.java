package com.lbi.swansonb.games.xo.server;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedList;
import java.util.List;

@ServerEndpoint(value = "/xo")
public class XOEndPoint {

	LinkedList<Session> queued;
	List<Game> games;

	public XOEndPoint(){
		queued = new LinkedList<Session>();
	}

	@OnMessage
    public String onMessage(String message, Session session) {
		String id = session.getId();
		if(isGaming(id)){

		} else if(isQueued(id)) {

		} else {

		}

		return message;
    }

	public boolean isQueued(String id) {
		return queued.contains(id);
	}

	public boolean isGaming(String id) {
		for(Game game : games) {
			if(game.isPlayerInGame(id)){
				return true;
			}
		}

		return false;
	}
}