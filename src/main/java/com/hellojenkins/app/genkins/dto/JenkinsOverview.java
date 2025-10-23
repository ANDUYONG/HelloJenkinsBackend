package com.hellojenkins.app.genkins.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hellojenkins.app.genkins.dto.children.JenkinsLogEntry;
import com.hellojenkins.app.genkins.dto.children.JenkinsPipelineTree;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsOverview {
    @JsonProperty("jobName")
    private String jobName;
    
    @JsonProperty("branchName")
    private String branchName;
    
    @JsonProperty("status")
    private String status;

    @JsonProperty("buildNumber")
    private int buildNumber;

    @JsonProperty("tree")
    private JenkinsPipelineTree tree;

    @JsonProperty("logs")
    private List<JenkinsLogEntry> logs;
    
    @JsonProperty("totalLog")
    private String totalLog;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public JenkinsPipelineTree getTree() {
		return tree;
	}

	public void setTree(JenkinsPipelineTree tree) {
		this.tree = tree;
	}

	public List<JenkinsLogEntry> getLogs() {
		return logs;
	}

	public void setLogs(List<JenkinsLogEntry> logs) {
		this.logs = logs;
	}

	public String getTotalLog() {
		return totalLog;
	}

	public void setTotalLog(String totalLog) {
		this.totalLog = totalLog;
	}
    
    
}
