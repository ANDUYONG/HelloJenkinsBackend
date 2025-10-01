package com.hellojenkins.app.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hellojenkins.app.github.dto.children.Author;
import com.hellojenkins.app.github.dto.children.Tree;
import com.hellojenkins.app.github.dto.children.Verification;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
	@JsonProperty("author")
    private Author author;
	@JsonProperty("committer")
    private Author committer;
	@JsonProperty("message")
    private String message;
	@JsonProperty("tree")
    private Tree tree;
	@JsonProperty("url")
    private String url;
    @JsonProperty("comment_count")
    private int commentCount;
    @JsonProperty("verification")
    private Verification verification;
}
