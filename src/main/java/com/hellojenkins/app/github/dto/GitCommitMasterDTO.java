package com.hellojenkins.app.github.dto;

import java.util.List;

import lombok.Data;

@Data
public class GitCommitMasterDTO {
    private List<GitFileContentDTO> list;
    private String branch;
	public List<GitFileContentDTO> getList() {
		return list;
	}
	public void setList(List<GitFileContentDTO> list) {
		this.list = list;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
    
    
}
