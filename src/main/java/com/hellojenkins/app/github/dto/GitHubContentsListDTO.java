package com.hellojenkins.app.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubContentsListDTO {
	@JsonProperty("name")
    private String name;
	@JsonProperty("path")
    private String path;
	@JsonProperty("size")
    private Long size;
	@JsonProperty("url")
    private String url;
	@JsonProperty("html_url")
    private String htmlUrl;
	@JsonProperty("git_url")
    private String gitUrl;
	@JsonProperty("download_url")
    private String downloadUrl;
	@JsonProperty("type")
    private String type;
	@JsonProperty("_links")
    private Links links;
	
	@Data
    @JsonIgnoreProperties(ignoreUnknown = true)
	public class Links {
		@JsonProperty("self")
	    private String self;
		@JsonProperty("git")
	    private String git;
		@JsonProperty("html")
	    private String html;
	}
}
