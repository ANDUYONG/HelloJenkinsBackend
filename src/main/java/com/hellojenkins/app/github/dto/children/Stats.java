package com.hellojenkins.app.github.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {
	@JsonProperty("total")
    private int total;
	@JsonProperty("additions")
    private int additions;
	@JsonProperty("deletions")
    private int deletions;
}
