package com.hellojenkins.app.github.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class File {
	@JsonProperty("sha")
    private String sha;
	@JsonProperty("filename")
    private String filename;
	@JsonProperty("status")
    private String status;
	@JsonProperty("additions")
    private int additions;
	@JsonProperty("deletions")
    private int deletions;
	@JsonProperty("changes")
    private int changes;

    @JsonProperty("blob_url")
    private String blobUrl;

    @JsonProperty("raw_url")
    private String rawUrl;

    @JsonProperty("contents_url")
    private String contentsUrl;

    @JsonProperty("patch")
    private String patch;
}
