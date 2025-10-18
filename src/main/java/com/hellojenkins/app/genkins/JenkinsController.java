package com.hellojenkins.app.genkins;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellojenkins.app.genkins.dto.JenkinsEvent;
import com.hellojenkins.app.genkins.dto.JenkinsExecutionNode;
import com.hellojenkins.app.genkins.dto.JenkinsOverview;
import com.hellojenkins.app.genkins.dto.children.JenkinsStageFlowNode;
import com.hellojenkins.app.genkins.websocket.JenkinsWebSocketHandler;

@RestController
@RequestMapping("/api/jenkins")
public class JenkinsController {
	private final JenkinsWebSocketHandler webSocketHandler;
	private final JenkinsProperties properties;

	public JenkinsController(JenkinsWebSocketHandler webSocketHandler, JenkinsProperties properties) {
		this.webSocketHandler = webSocketHandler;
		this.properties = properties;
	}
	
    @GetMapping("/overview/{buildNumber}/{node}")
    public ResponseEntity<?> getLog(
    		@RequestParam(name = "branchName", defaultValue = "main") String branchName,
    		@PathVariable("buildNumber") String buildNumber,
    		@PathVariable("node") String node
		) {
		try {
			// 1. branchName을 1차 인코딩 (feature/test1 -> feature%2Ftest1)
			String encodedBranchName = URLEncoder.encode(branchName, StandardCharsets.UTF_8.toString());
			String doubleEncodedBranchName = URLEncoder.encode(encodedBranchName, StandardCharsets.UTF_8.toString());
			// 2. 1차 인코딩된 문자열을 다시 2차 인코딩 (feature%2Ftest1 -> feature%252Ftest1)
			
			String url = String.format(
					"%s%s/%s/execution/node/%s/wfapi/describe", 
					properties.getBaseUrl(), 
					doubleEncodedBranchName,
					buildNumber,
					node);
			
			ResponseEntity<String> response = this.getResponseData(url); 
			String jsonBody = response.getBody();

	        // 2. ObjectMapper를 사용하여 JSON 문자열을 Java 객체로 변환 (핵심!)
	        ObjectMapper objectMapper = new ObjectMapper();
	        JenkinsExecutionNode executionNode = objectMapper.readValue(jsonBody, JenkinsExecutionNode.class);
			
	        // 3. 마지막 노드 뽑아오기 : 마지막 노드에 로그 존
	        List<JenkinsStageFlowNode> list = executionNode.getStageFlowNodes();
	        List<String> strList = list.stream()
	        		.map(x -> x.getParameterDescription())
	        		.filter(x -> x != null)
	        		.collect(Collectors.toList());
	        
			// X-Frame-Options 제거
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<>(strList, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
		}
    }
	
	@PostMapping("/event")
	public ResponseEntity<Void> receiveJenkinsEvent(@RequestBody JenkinsEvent event) {
		// event: jobName, buildNunber, stage, status, logs
		String json;
		try {
			json = new ObjectMapper().writeValueAsString(event);

			// 프론트로 실시간 전송
			webSocketHandler.sendEventMessage(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/event/overview")
	public ResponseEntity<Void> receiveJenkinsEventOverview(@RequestBody JenkinsOverview event) {
		// event: jobName, buildNunber, stage, status, logs
		String json;
		try {
			json = new ObjectMapper().writeValueAsString(event);

			// 프론트로 실시간 전송
			webSocketHandler.sendOverviewMessage(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return ResponseEntity.ok().build();
	}
	
	private ResponseEntity<String> getResponseData(String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(properties.getUser(), properties.getToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response;
	}
}
