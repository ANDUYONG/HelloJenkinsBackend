package com.hellojenkins.app.genkins.dto.children;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Jenkins Pipeline 상세 데이터 DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsPipelineData {
    @JsonProperty("complete")
    private Boolean complete;

    @JsonProperty("stages")
    private List<JenkinsStage> stages;
}

