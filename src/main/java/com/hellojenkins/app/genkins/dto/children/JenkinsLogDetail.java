package com.hellojenkins.app.genkins.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Jenkins 로그 상세 정보 DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsLogDetail {
    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private JenkinsLogData data;
}
