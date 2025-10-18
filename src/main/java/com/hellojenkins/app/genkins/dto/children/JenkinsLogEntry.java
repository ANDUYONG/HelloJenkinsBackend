package com.hellojenkins.app.genkins.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Jenkins 로그 목록의 개별 항목 DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsLogEntry {
    @JsonProperty("id")
    private String id;

    @JsonProperty("log")
    private JenkinsLogDetail log;
}