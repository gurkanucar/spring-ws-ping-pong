package com.gucardev.springwspingpong;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configs implements HttpSessionListener {
  public void sessionCreated(HttpSessionEvent event) {
    event.getSession().setMaxInactiveInterval(24 * 60 * 60);
  }
}
