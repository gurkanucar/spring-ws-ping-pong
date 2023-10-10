package com.gucardev.springwspingpong;

import java.io.IOException;
import java.util.Arrays;
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

  private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    log.info("message: {}", message.getPayload());
    String payload = message.getPayload();

    if (payload.startsWith("ls")) {
      handleListSessions(session);
    } else if (payload.startsWith("ba-")) {
      broadcastMessageToAll(payload);
    } else if (payload.startsWith("bo-")) {
      broadcastMessageToOthers(session, payload);
    } else {
      sendBackToSender(session, payload);
    }
  }

  private void handleListSessions(WebSocketSession session) throws IOException {
    log.info(Arrays.toString(sessions.toArray()));
    var array = sessions.stream().map(WebSocketSession::getId).toArray();
    String sessionIds = Arrays.toString(array);
    session.sendMessage(new TextMessage("clients: %d | %s".formatted(array.length, sessionIds)));
  }

  private void broadcastMessageToAll(String message) throws IOException {
    for (WebSocketSession webSocketSession : sessions) {
      webSocketSession.sendMessage(new TextMessage(message));
    }
  }

  private void broadcastMessageToOthers(WebSocketSession sender, String message)
      throws IOException {
    for (WebSocketSession webSocketSession : sessions) {
      if (!webSocketSession.getId().equals(sender.getId())) {
        webSocketSession.sendMessage(new TextMessage(message));
      }
    }
  }

  private void sendBackToSender(WebSocketSession session, String message) throws IOException {
    session.sendMessage(new TextMessage(message));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    log.info("new session connected {}", session.toString());
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
  }
}
