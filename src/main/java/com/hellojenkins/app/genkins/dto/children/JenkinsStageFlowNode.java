package com.hellojenkins.app.genkins.dto.children;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsStageFlowNode {
	@JsonProperty("_links")
    private JenkinsLinks links;
	@JsonProperty("id")
    private String id;
	@JsonProperty("name")
    private String name;
	@JsonProperty("execNode")
    private String execNode;
	@JsonProperty("status")
    private String status;
	@JsonProperty("parameterDescription")
    private String parameterDescription;
	@JsonProperty("startTimeMillis")
    private long startTimeMillis;
	@JsonProperty("durationMillis")
    private long durationMillis;
	@JsonProperty("pauseDurationMillis")
    private long pauseDurationMillis;
	@JsonProperty("parentNodes")
    private List<String> parentNodes;
	public JenkinsLinks getLinks() {
		return links;
	}
	public void setLinks(JenkinsLinks links) {
		this.links = links;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExecNode() {
		return execNode;
	}
	public void setExecNode(String execNode) {
		this.execNode = execNode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getParameterDescription() {
		return parameterDescription;
	}
	public void setParameterDescription(String parameterDescription) {
		this.parameterDescription = parameterDescription;
	}
	public long getStartTimeMillis() {
		return startTimeMillis;
	}
	public void setStartTimeMillis(long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}
	public long getDurationMillis() {
		return durationMillis;
	}
	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}
	public long getPauseDurationMillis() {
		return pauseDurationMillis;
	}
	public void setPauseDurationMillis(long pauseDurationMillis) {
		this.pauseDurationMillis = pauseDurationMillis;
	}
	public List<String> getParentNodes() {
		return parentNodes;
	}
	public void setParentNodes(List<String> parentNodes) {
		this.parentNodes = parentNodes;
	}
	
	
}
