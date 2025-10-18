package com.hellojenkins.app.genkins.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsEvent {
	@JsonProperty("jobName")
	private String jobName;
	@JsonProperty("branch")
	private String branch;
	@JsonProperty("buildNumber")
	private String buildNumber;
	@JsonProperty("stage")
	private String stage;
	@JsonProperty("status")
	private String status;
	@JsonProperty("logs")
	private String logs;
}
