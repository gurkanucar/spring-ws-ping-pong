package com.gucardev.springwspingpong;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
//
// @ServerEndpoint(value = "/ws/endpoint")
// public class MyWebSocket {
//  @OnOpen
//  public void onOpen(Session session) {
//    System.out.println("Open Connection ...");
//  }
//
//  @OnClose
//  public void onClose(Session session) {
//    System.out.println("Close Connection ...");
//  }
//
//  @OnMessage
//  public void onMessage(String message, Session session) {
//    System.out.println("Message from the client: " + message);
//    String echoMsg = "Echo from the server : " + message;
//    try {
//      session.getBasicRemote().sendText(echoMsg);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @OnError
//  public void onError(Throwable throwable) {
//    throwable.printStackTrace();
//  }
// }
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

  List<Object> sessions = new CopyOnWriteArrayList<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    session.sendMessage(new TextMessage(message.getPayload()));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // the messages will be broadcasted to all users.
    log.info("new session connected {}", session.toString());
    sessions.add(session);
  }
}
