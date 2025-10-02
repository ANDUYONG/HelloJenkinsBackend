package com.hellojenkins.app.genkins;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellojenkins.app.genkins.dto.JenkinsEvent;
import com.hellojenkins.app.genkins.websocket.JenkinsWebSocketHandler;

@RestController
@RequestMapping("/api/jenkins")
public class JenkinsController {
	private final JenkinsWebSocketHandler webSocketHandler;

	public JenkinsController(JenkinsWebSocketHandler webSocketHandler) {
		this.webSocketHandler = webSocketHandler;
	}

	@PostMapping("/event")
	public ResponseEntity<Void> receiveJenkinsEvent(@RequestBody JenkinsEvent event) {
		// event: jobName, buildNunber, stage, status, logs
		String json;
		try {
			json = new ObjectMapper().writeValueAsString(event);

			// 프론트로 실시간 전송
			webSocketHandler.sendMessage(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return ResponseEntity.ok().build();
	}
}
