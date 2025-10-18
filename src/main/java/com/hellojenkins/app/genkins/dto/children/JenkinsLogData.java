package com.hellojenkins.app.genkins.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Jenkins 로그 데이터 DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsLogData {
    @JsonProperty("text")
    private String text;

    @JsonProperty("startByte")
    private Long startByte;

    @JsonProperty("endByte")
    private Long endByte;

    @JsonProperty("nodeIsActive")
    private Boolean nodeIsActive;
}
