package com.hellojenkins.app.genkins.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Jenkins Pipeline 구조 정보 DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsPipelineTree {
    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private JenkinsPipelineData data;
}