package com.hellojenkins.app.github.dto;

import java.util.List;

import lombok.Data;

@Data
public class GitBranchMergeRequest {
	private List<String> branchNames; // 병합할 브랜치 이름 배열 (순서 중요)
    private String baseBranch;        // dev 또는 main 브랜치
    
	public List<String> getBranchNames() {
		return branchNames;
	}
	public void setBranchNames(List<String> branchNames) {
		this.branchNames = branchNames;
	}
	public String getBaseBranch() {
		return baseBranch;
	}
	public void setBaseBranch(String baseBranch) {
		this.baseBranch = baseBranch;
	}
    
    
}
