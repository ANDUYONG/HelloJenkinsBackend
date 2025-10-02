package com.hellojenkins.app.github;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hellojenkins.app.github.dto.GitCommitSummaryDTO;
import com.hellojenkins.app.github.dto.GitFileContentDTO;
import com.hellojenkins.app.github.dto.GitTrreDTO;

@RestController
@RequestMapping("/api/github")
public class GitHubController {
	private final GitHubService gitHubService;

	public GitHubController(GitHubService gitHubService) {
		this.gitHubService = gitHubService;
	}

	// http://localhost:8080/github/latestCommit?branch=main
	@GetMapping("/latestCommit")
	public GitCommitSummaryDTO latestCommit(@RequestParam(name = "branch", defaultValue = "main") String branch) {
		return gitHubService.getLatestCommitSha(branch);
	}

	// http://localhost:8080/github/files?commitSha=e0303450fb3a917717123768bcc2007379b31fe9
    @GetMapping("/files")
    public GitTrreDTO files() {
        return gitHubService.getFilesAtCommit();
    }

    // http://localhost:8080/github/file?branch=main
    @GetMapping("/file")
    public GitFileContentDTO fileContent(@RequestParam(name = "filePath") String filePath,
                              @RequestParam(name = "branch", defaultValue = "main") String branch) {
        return gitHubService.getFileContent(filePath, branch);
    }

    @PostMapping("/commitAndPush")
    public int commitAndPush(@RequestBody List<GitFileContentDTO> list) {
    	int result = gitHubService.commitAndPush(list);
    	return result;
    }
}
