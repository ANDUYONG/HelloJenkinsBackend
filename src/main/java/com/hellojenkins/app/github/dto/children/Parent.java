package com.hellojenkins.app.github.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parent {
	@JsonProperty("sha")
    private String sha;
	@JsonProperty("url")
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;
}
