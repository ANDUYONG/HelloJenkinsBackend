package com.hellojenkins.app.github;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.hellojenkins.app.github.dto.GitBranchDTO;
import com.hellojenkins.app.github.dto.GitCommitSummaryDTO;
import com.hellojenkins.app.github.dto.GitFileContentDTO;
import com.hellojenkins.app.github.dto.GitTrreDTO;
import com.hellojenkins.app.github.dto.GitTrreDTO.TreeNode;
import com.hellojenkins.app.slack.SlackService;


@Service
public class GitHubService {

   private final GitHubProperties properties;
   private final SlackService slackService;
   private final RestTemplate restTemplate = new RestTemplate();
   private final ObjectMapper mapper = new ObjectMapper();

   public GitHubService(GitHubProperties properties, SlackService slackService) {
	   this.properties = properties;
	   this.slackService = slackService;
	   mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
   }

   private HttpHeaders getHeaders() {
	   HttpHeaders headers = new HttpHeaders();
	   headers.set("Authorization", "token " + properties.getToken());
	   headers.set("Accept", "application/vnd.github.v3+json");
	   return headers;
   }
   
	// -----------------------------------------------------------
	// 핵심 로직: 순차적 강제 덮어쓰기
	// -----------------------------------------------------------
	
	public String sequentialOverwrite(List<String> branchNames, String baseBranch) {
	    String baseSha = null; // baseSha는 첫 번째 브랜치 병합 후 업데이트되어야 함.
	
	    for (String headBranch : branchNames) {
	        try {
	            // 1. Head 브랜치의 최신 커밋 SHA 가져오기
	            String headSha = this.getBranchHeadSha(headBranch);
	            
	            // 2. Base 브랜치에 강제 덮어쓰기 (force push 효과)
	            this.overwriteBaseBranch(headBranch, baseBranch, headSha);
	
	        } catch (Exception e) {
	            // 특정 브랜치 덮어쓰기 실패 시 예외 처리
	            throw new RuntimeException("Error overwriting " + baseBranch + " with " + headBranch + ": " + e.getMessage(), e);
	        }
	    }
	    
	    String commitUrl = String.format(
	    		"%s/%s/%s/tree/%s", 
	    		properties.getBaseUrl(),
	    		properties.getOwner(),
	    		properties.getRepo(),
	    		baseBranch
		); 
	    String msg = baseBranch.equals("dev") ? "feature > dev 병합완료" : "dev > main 병합완료"; 
	    slackService.sendFromGithub(properties.getRepo(), properties.getOwner(), msg, commitUrl, baseBranch);
	    return "All branches successfully overwrote " + baseBranch;
	}
	
	/**
	 * 1. 특정 브랜치의 HEAD 커밋 SHA를 가져옵니다.
	 * GET /repos/{owner}/{repo}/git/refs/heads/{branch}
	 */
	private String getBranchHeadSha(String branchName) {
	    String url = String.format(
	        "%s/repos/%s/%s/git/refs/heads/%s", 
	        properties.getApiUrl(), 
	        properties.getOwner(), 
	        properties.getRepo(), 
	        branchName
	    );
	
	    HttpEntity<Void> entity = new HttpEntity<>(getHeaders());
	    
	    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
	    
	    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	        Map<String, String> object = (Map<String, String>) response.getBody().get("object");
	        return object.get("sha"); // 커밋 SHA 반환
	    }
	    throw new RuntimeException("Failed to get SHA for branch: " + branchName);
	}
	
	/**
	 * 2. Base 브랜치를 Head 브랜치의 SHA로 강제 업데이트합니다 (덮어쓰기).
	 * PUT /repos/{owner}/{repo}/git/refs/heads/{baseBranch}
	 */
	private void overwriteBaseBranch(String headBranch, String baseBranch, String headSha) {
	    String url = String.format(
	        "%s/repos/%s/%s/git/refs/heads/%s", 
	        properties.getApiUrl(), 
	        properties.getOwner(), 
	        properties.getRepo(), 
	        baseBranch
	    );
	
	    // 요청 바디: 새로운 SHA와 강제 푸시 옵션
	    Map<String, Object> overwriteBody = Map.of(
	        "sha", headSha,
	        "force", true // 이 부분이 git push --force와 동일한 역할을 합니다.
	    );
	
	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(overwriteBody, getHeaders());
	
	    ResponseEntity<Void> response = restTemplate.exchange(
	        url, 
	        HttpMethod.PUT, 
	        entity, 
	        Void.class // 응답 본문은 필요 없음
	    );
	    
	    if (!response.getStatusCode().is2xxSuccessful()) {
	        throw new RuntimeException(String.format(
	            "Force overwrite failed (Status: %s) while merging %s into %s.", 
	            response.getStatusCode(), headBranch, baseBranch
	        ));
	    }
	    System.out.printf("Successfully overwrote %s with %s's HEAD (%s).\n", baseBranch, headBranch, headSha);
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
   public GitTrreDTO getFilesAtCommit(String branch) {
       String url = String.format("%s/repos/%s/%s/git/trees/%s?recursive=1",
               properties.getApiUrl(),
               properties.getOwner(),
               properties.getRepo(),
               this.getMainBranchTreeSha(branch));

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
   
   // 브랜치 목록 조회 -> feature
   public List<GitBranchDTO> branches() {
	   String url = String.format("%s/repos/%s/%s/branches",
			   properties.getApiUrl(),
               properties.getOwner(),
               properties.getRepo());

       HttpEntity<String> entity = new HttpEntity<>(this.getHeaders());
       ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

       try {
    	   List<GitBranchDTO> result = mapper.readValue(response.getBody(), new TypeReference<List<GitBranchDTO>>() {});
           return result.stream()
        		    .filter(x -> x.getName() != null && x.getName().startsWith("feature/"))
        		   	.collect(Collectors.toList());
       } catch (JsonProcessingException e) {
           throw new RuntimeException("GitHub tree JSON 파싱 실패", e);
       }
   }
   
   public String createBranch(String newBranchName) {
	   // 1. base branch의 최 sha값 가져오
       String refUrl = String.format("%s/repos/%s/%s/git/ref/heads/feature", 
    		   properties.getApiUrl(),
    		   properties.getOwner(), 
    		   properties.getRepo());
       ResponseEntity<Map> refResponse = restTemplate.exchange(refUrl, HttpMethod.GET, new HttpEntity<>(getHeaders()), Map.class);

       String sha = (String) ((Map<String, Object>) refResponse.getBody().get("object")).get("sha");

       // 2. 새 브랜치 생성
       String createUrl = String.format("%s/repos/%s/%s/git/refs", 
    		   properties.getApiUrl(),
    		   properties.getOwner(), 
    		   properties.getRepo());

       Map<String, String> body = new HashMap<>();
       body.put("ref", "refs/heads/feature/" + newBranchName);
       body.put("sha", sha);

       ResponseEntity<String> createResponse = restTemplate.exchange(
               createUrl,
               HttpMethod.POST,
               new HttpEntity<>(body, getHeaders()),
               String.class
       );

       return createResponse.getBody();
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

   private String getMainBranchTreeSha(String branch) {
	    String url = String.format("%s/repos/%s/%s/branches/%s",
	            properties.getApiUrl(),
	            properties.getOwner(),
	            properties.getRepo(),
	            branch);

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

   public int commitAndPush(List<GitFileContentDTO> list, String branch) {
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

	    		GitFileContentDTO latest = this.getFileContent(dto.getPath(), branch);
	    		String commitMsg = dto.getMessage();
	    		String encodedData = dto.getEncodedData();

	    		// Request body
	    		Map<String, Object> body = new HashMap<>();
	    		body.put("message", commitMsg);
	    		body.put("content", encodedData);
	    		body.put("branch", branch);  // 파라미터로 브랜치 지정
	    		body.put("sha", latest.getSha());  // 수정일 경우 필요

	    		// Headers
	    		HttpHeaders headers = new HttpHeaders();
	    		headers.setContentType(MediaType.APPLICATION_JSON);
	    		headers.setBearerAuth(properties.getToken());  // 토큰 사용

	    		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

	    		// API 호출
	    		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
	    		System.out.println("Response for " + dto.getPath() + ": " + response.getBody());
	    		
	    	    String commitUrl = String.format(
	    	    		"%s/%s/%s/tree/%s", 
	    	    		properties.getBaseUrl(),
	    	    		properties.getOwner(),
	    	    		properties.getRepo(),
	    	    		branch
	    		); 
	    	    slackService.sendFromGithub(properties.getRepo(), properties.getOwner(), commitMsg, commitUrl, branch);
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
