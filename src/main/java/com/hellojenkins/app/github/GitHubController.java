package com.hellojenkins.app.github;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hellojenkins.app.github.dto.GitBranchDTO;
import com.hellojenkins.app.github.dto.GitBranchMergeRequest;
import com.hellojenkins.app.github.dto.GitCommitMasterDTO;
import com.hellojenkins.app.github.dto.GitCommitSummaryDTO;
import com.hellojenkins.app.github.dto.GitFileContentDTO;
import com.hellojenkins.app.github.dto.GitTrreDTO;
import com.hellojenkins.app.slack.SlackService;

@RestController
@RequestMapping("/api/github")
public class GitHubController {
	private final GitHubService gitHubService;
	private final SlackService slackService;

	public GitHubController(GitHubService gitHubService, SlackService slackService) {
		this.gitHubService = gitHubService;
		this.slackService = slackService;
	}

	// http://localhost:8080/github/latestCommit?branch=main
	@GetMapping("/latestCommit")
	public GitCommitSummaryDTO latestCommit(@RequestParam(name = "branch", defaultValue = "main") String branch) {
		return gitHubService.getLatestCommitSha(branch);
	}

	// http://localhost:8080/github/files?commitSha=e0303450fb3a917717123768bcc2007379b31fe9
    @GetMapping("/files")
    public GitTrreDTO files(@RequestParam(name = "branch", defaultValue = "main") String branch) {
        return gitHubService.getFilesAtCommit(branch);
    }

    // http://localhost:8080/github/file?branch=main
    @GetMapping("/file")
    public GitFileContentDTO fileContent(@RequestParam(name = "filePath") String filePath,
                              @RequestParam(name = "branch") String branch) {
        return gitHubService.getFileContent(filePath, branch);
    }
    
    @GetMapping("/branches")
    public List<GitBranchDTO> branches() {
        return gitHubService.branches();
    }
    
    @PostMapping("/createBranch")
    public String createBranch(
            @RequestParam(name = "branch") String newBranch) {
        String result = gitHubService.createBranch(newBranch);
        return result;
    }

    @PostMapping("/commitAndPush")
    public int commitAndPush(@RequestBody GitCommitMasterDTO dto) {
    	int result = gitHubService.commitAndPush(dto.getList(), dto.getBranch());
    	
    	return result;
    }
    
    @PostMapping("mergeBranches")
    public ResponseEntity<?> mergeBranches(@RequestBody GitBranchMergeRequest request) {
    	try {
    		String result = gitHubService.sequentialOverwrite(request.getBranchNames(), request.getBaseBranch());
            return ResponseEntity.ok(result);
    	} catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Merge failed: " + e.getMessage());
    	}
    }
}
