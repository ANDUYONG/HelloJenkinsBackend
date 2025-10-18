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

    @JsonProperty("buildNumber")
    private int buildNumber;

    @JsonProperty("tree")
    private JenkinsPipelineTree tree;

    @JsonProperty("logs")
    private List<JenkinsLogEntry> logs;
    
    @JsonProperty("totalLog")
    private String totalLog;
}
