package com.lbi.swansonb.games.xo.client;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DocClient {
    private static CountDownLatch messageLatch;
    private static final String SENT_MESSAGE = "Hello World";

    public static void main(String [] args){
        try {
            messageLatch = new CountDownLatch(1);

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

            ClientManager client = ClientManager.createClient();
            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.addMessageHandler(new MessageHandler.Whole<String>() {

                            @Override
                            public void onMessage(String message) {
                                System.out.println("Received message: "+message);
                                messageLatch.countDown();
                            }
                        });
                        session.getBasicRemote().sendText(SENT_MESSAGE);
	                    Thread.sleep(10000);
                        session.getBasicRemote().sendText(SENT_MESSAGE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }, cec, new URI("ws://localhost:8025/websocket/xo"));
	        messageLatch.await(100, TimeUnit.SECONDS);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}