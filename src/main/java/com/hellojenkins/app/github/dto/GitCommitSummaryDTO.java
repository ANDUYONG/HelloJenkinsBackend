package com.hellojenkins.app.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hellojenkins.app.github.dto.children.Author;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitCommitSummaryDTO {
	@JsonProperty("sha")
	private String sha;

	@JsonProperty("node_id")
	private String nodeId;

	@JsonProperty("commit")
    private Commit commit;

	@JsonProperty("url")
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("committer")
    private Author committer;
}
