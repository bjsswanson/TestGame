package com.lbi.swansonb.games.xo.client;

import com.lbi.swansonb.games.xo.server.Game;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@ClientEndpoint
public class XORandomClient {
	private static CountDownLatch messageLatch;

	private Session session;
	private Random random;

	@OnOpen
    public void onOpen(Session session) {
        this.session = session;
		this.random = new Random();
		System.out.println("Connected to Server.");
    }

	 @OnMessage
    public void onMessage(String message) {
		System.out.println("Recieved: " + message);
        if(message.equals(Game.IT_IS_YOUR_TURN)){
			sendMessage(session, randomMove());
		}

        if(message.equals(Game.POSITION_TAKEN)) {
            sendMessage(session, randomMove());
        }
    }

	private String randomMove() {
		return ((Integer)random.nextInt(9)).toString();
	}


	private void sendMessage(Session session, String message){
        try {
	        System.out.println("Sending: " + message);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

	public static void main(String [] args){
	    try {
		    messageLatch = new CountDownLatch(1);
		    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		    String uri = "ws://localhost:8025/websocket/xo";
		    container.connectToServer(XORandomClient.class, URI.create(uri));
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}