package com.hellojenkins.app.genkins.dto.children;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Jenkins Pipeline의 개별 Stage DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsStage {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("state")
    private String state; // success, running 등

    @JsonProperty("type")
    private String type; // STAGE

    @JsonProperty("title")
    private String title;

    @JsonProperty("pauseDurationMillis")
    private Long pauseDurationMillis;

    @JsonProperty("startTimeMillis")
    private Long startTimeMillis;

    @JsonProperty("totalDurationMillis")
    private Long totalDurationMillis; // 진행 중인 Stage에서는 없을 수 있음

    @JsonProperty("children")
    private List<Object> children;

    @JsonProperty("isSequential")
    private Boolean isSequential;

    @JsonProperty("synthetic")
    private Boolean synthetic;

    @JsonProperty("placeholder")
    private Boolean placeholder;

    @JsonProperty("agent")
    private String agent;

    @JsonProperty("url")
    private String url;

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPauseDurationMillis() {
		return pauseDurationMillis;
	}

	public void setPauseDurationMillis(Long pauseDurationMillis) {
		this.pauseDurationMillis = pauseDurationMillis;
	}

	public Long getStartTimeMillis() {
		return startTimeMillis;
	}

	public void setStartTimeMillis(Long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}

	public Long getTotalDurationMillis() {
		return totalDurationMillis;
	}

	public void setTotalDurationMillis(Long totalDurationMillis) {
		this.totalDurationMillis = totalDurationMillis;
	}

	public List<Object> getChildren() {
		return children;
	}

	public void setChildren(List<Object> children) {
		this.children = children;
	}

	public Boolean getIsSequential() {
		return isSequential;
	}

	public void setIsSequential(Boolean isSequential) {
		this.isSequential = isSequential;
	}

	public Boolean getSynthetic() {
		return synthetic;
	}

	public void setSynthetic(Boolean synthetic) {
		this.synthetic = synthetic;
	}

	public Boolean getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(Boolean placeholder) {
		this.placeholder = placeholder;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
