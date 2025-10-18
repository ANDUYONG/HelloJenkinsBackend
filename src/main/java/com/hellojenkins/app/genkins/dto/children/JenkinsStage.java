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
}
