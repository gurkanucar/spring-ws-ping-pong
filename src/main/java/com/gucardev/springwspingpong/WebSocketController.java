package com.gucardev.springwspingpong;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send/ping")
    @SendTo("/topic/pong")
    public String handlePingMessage(String message) {
        if ("Ping".equalsIgnoreCase(message)) {
            return "Pong";
        }
        return "Message reach to server: "+message;
    }
}
