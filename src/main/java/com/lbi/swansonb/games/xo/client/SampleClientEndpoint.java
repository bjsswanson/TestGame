package com.lbi.swansonb.games.xo.client;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;

@ClientEndpoint
public class SampleClientEndpoint {

    @OnOpen
    public void onOpen(Session p) {
        try {
            p.getBasicRemote().sendText("Hello!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(String.format("%s %s", "Received message: ", message));
    }
}