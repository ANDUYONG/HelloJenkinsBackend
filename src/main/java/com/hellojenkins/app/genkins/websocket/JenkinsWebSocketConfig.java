package com.hellojenkins.app.genkins.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class JenkinsWebSocketConfig implements WebSocketConfigurer {
	private final JenkinsWebSocketHandler jenkinsWebSocketHandler;

	public JenkinsWebSocketConfig(JenkinsWebSocketHandler jenkinsWebSocketHandler) {
		this.jenkinsWebSocketHandler = jenkinsWebSocketHandler;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(jenkinsWebSocketHandler, "/ws/jenkins")
			.setAllowedOriginPatterns("*");
		registry.addHandler(jenkinsWebSocketHandler, "/ws/overview")
			.setAllowedOriginPatterns("*");
	}
}
