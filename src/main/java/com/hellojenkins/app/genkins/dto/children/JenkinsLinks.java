package com.hellojenkins.app.genkins.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsLinks {
	@JsonProperty("self")
	private JenkinsHref self;
	@JsonProperty("log")
    private JenkinsHref log;
	@JsonProperty("console")
    private JenkinsHref console;
}