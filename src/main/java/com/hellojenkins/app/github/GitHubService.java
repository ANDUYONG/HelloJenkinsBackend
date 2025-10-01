package com.hellojenkins.app.github;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.hellojenkins.app.github.dto.GitCommitSummaryDTO;
import com.hellojenkins.app.github.dto.GitFileContentDTO;
import com.hellojenkins.app.github.dto.GitTrreDTO;
import com.hellojenkins.app.github.dto.GitTrreDTO.TreeNode;


@Service
public class GitHubService {
	
   private final GitHubProperties properties;
   private final RestTemplate restTemplate = new RestTemplate();
   private final ObjectMapper mapper = new ObjectMapper();
   
   public GitHubService(GitHubProperties properties) {
	   this.properties = properties;
	   mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
   }
   
   private HttpHeaders getHeaders() {
	   HttpHeaders headers = new HttpHeaders();
	   headers.set("Authorization", "token " + properties.getToken());
	   headers.set("Accept", "application/vnd.github.v3+json");
	   return headers;
   }
   
   // 최신 커밋 SHA조회
   public GitCommitSummaryDTO getLatestCommitSha(String branch) {
       String url = String.format("%s/repos/%s/%s/commits/%s",
               properties.getApiUrl(),
               properties.getOwner(),
               properties.getRepo(),
               branch);

       HttpEntity<String> entity = new HttpEntity<>(this.getHeaders());

       // 1. 문자열로 받아오기
       ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

       try {
           // 2. ObjectMapper로 DTO 변환
           return mapper.readValue(response.getBody(), GitCommitSummaryDTO.class);
       } catch (JsonProcessingException e) {
           throw new RuntimeException("GitHub commit JSON 파싱 실패", e);
       }
   }
   
   
   // 특정 커밋 기준 파일 목록 조회
   public GitTrreDTO getFilesAtCommit() {
       String url = String.format("%s/repos/%s/%s/git/trees/%s?recursive=1",
               properties.getApiUrl(),
               properties.getOwner(),
               properties.getRepo(),
               this.getMainBranchTreeSha());

       HttpEntity<String> entity = new HttpEntity<>(this.getHeaders());
       ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

       try {
    	   GitTrreDTO dto = mapper.readValue(response.getBody(), GitTrreDTO.class);
    	   dto.setTree(this.getTreeNode(dto.getTree(), ""));
           return dto;
       } catch (JsonProcessingException e) {
           throw new RuntimeException("GitHub tree JSON 파싱 실패", e);
       }
   }
   
   // 개별 파일 조회
   public GitFileContentDTO getFileContent(String filePath, String branch) {
       String url = String.format("%s/repos/%s/%s/contents/%s?ref=%s",
               properties.getApiUrl(),
               properties.getOwner(),
               properties.getRepo(),
               filePath,
               branch);

       HttpEntity<String> entity = new HttpEntity<>(this.getHeaders());
       ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

       try {
           return mapper.readValue(response.getBody(), GitFileContentDTO.class);
       } catch (JsonProcessingException e) {
           throw new RuntimeException("GitHub file JSON 파싱 실패", e);
       }
   }
   
   private String getMainBranchTreeSha() {
	    String url = String.format("%s/repos/%s/%s/branches/test",
	            properties.getApiUrl(),
	            properties.getOwner(),
	            properties.getRepo());

	    HttpEntity<String> entity = new HttpEntity<>(this.getHeaders());
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	    try {
	        JsonNode node = mapper.readTree(response.getBody());
	        return node.path("commit").path("commit").path("tree").path("sha").asText();
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("브랜치 JSON 파싱 실패", e);
	    }
	}
   
   private List<TreeNode> getTreeNode(List<TreeNode> allNodes, String parentPath) {
	   Set<String> addedPaths = new HashSet<>(); // 중복 방지용 
	   
	   // DFS 알고리즘 적용
	   // 파일 하이라키 구조의 데이터를 표현하기 위함.
	   List<TreeNode> children = allNodes.stream()
	            .filter(node -> {
	            	String nodeParent = getParentPath(node.getPath());
	            	if(parentPath.length() > 0) {	            		
	            		return nodeParent.equals(parentPath);
	            	} else {
	            		// 최상위 노드일 때, 이미 다른 부모로 추가된 노드면 제외
	                    return !addedPaths.contains(node.getPath()) && nodeParent.isEmpty();
	            	}
	            })
	            .map(node -> {
	            	node.setFilePath(getParentPath(node.getPath()));
	            	node.setFileName(getFileName(node.getPath()));
	            	
	            	addedPaths.add(node.getPath()); // 이미 사용된 경로 기록
	            	
	                if ("tree".equals(node.getType())) {
	                    node.setFile(false);
	                    
	                    // 폴더 일 경우, 재귀적으로 데이터를 추가한다.
	                    node.setChildren(this.getTreeNode(allNodes, node.getPath())); // 바로 아래 자식만
	                    
	                } else {
	                    node.setFile(true);
	                }
	                return node;
	            })
	            .sorted(Comparator
	                    .comparing(TreeNode::isFile) // false 먼저, true 나중
	                    .thenComparing(TreeNode::getFileName, Comparator.naturalOrder()) // 이름 순 정렬
	            )
	            .toList();
	    return children;
	}
   
   public int commitAndPush(List<GitFileContentDTO> list) {
	    try {
	    	
	    	for (GitFileContentDTO dto : list) {
	    		// GitHub API URL
	    		String url = String.format(
	    				"%s/repos/%s/%s/contents/%s", 
	    				properties.getApiUrl(), 
	    				properties.getOwner(), 
	    				properties.getRepo(),
	    				dto.getPath()
	    				);
	    		
	    		GitFileContentDTO latest = this.getFileContent(dto.getPath(), "test");
	    		String message = dto.getMessage();
	    		String encodedData = dto.getEncodedData();
	    		
	    		// Request body
	    		Map<String, Object> body = new HashMap<>();
	    		body.put("message", message);
	    		body.put("content", encodedData);
	    		body.put("branch", "test");  // 파라미터로 브랜치 지정
	    		body.put("sha", latest.getSha());  // 수정일 경우 필요
	    		
	    		// Headers
	    		HttpHeaders headers = new HttpHeaders();
	    		headers.setContentType(MediaType.APPLICATION_JSON);
	    		headers.setBearerAuth(properties.getToken());  // 토큰 사용
	    		
	    		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
	    		
	    		// API 호출
	    		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
	    		System.out.println("Response for " + dto.getPath() + ": " + response.getBody());
	    	}
	    } catch(Exception e) {
	    	System.out.println(e);
	    	throw e;
	    }

	    return list.size();
	}

   
   private String getParentPath(String path) {
	    int lastIndex = path.lastIndexOf('/');
	    return lastIndex == -1 ? "" : path.substring(0, lastIndex);
	}

	private String getFileName(String path) {
	    int lastIndex = path.lastIndexOf('/');
	    return lastIndex == -1 ? path : path.substring(lastIndex + 1);
	}
}
