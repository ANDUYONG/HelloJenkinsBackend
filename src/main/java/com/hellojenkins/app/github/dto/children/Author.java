package com.hellojenkins.app.github.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
	@JsonProperty("name")
    private String name;
	@JsonProperty("email")
    private String email;
	@JsonProperty("date")
    private String date;
}