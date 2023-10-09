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
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler {

  List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    log.info("message: {}", message);

    if (message.getPayload().startsWith("ba-")) {
      // Broadcast to all
      for (WebSocketSession webSocketSession : sessions) {
        webSocketSession.sendMessage(new TextMessage(message.getPayload()));
      }
    } else if (message.getPayload().startsWith("bo-")) {
      // Broadcast to others (exclude current session)
      for (WebSocketSession webSocketSession : sessions) {
        if (!webSocketSession.getId().equals(session.getId())) {
          webSocketSession.sendMessage(new TextMessage(message.getPayload()));
        }
      }
    } else {
      session.sendMessage(new TextMessage(message.getPayload()));
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("new session connected {}", session.toString());
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessions.remove(session);
  }
}
