package com.hellojenkins.app.github.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonProperty("login")
    private String login;
	@JsonProperty("id")
    private long id;

    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("url")
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("type")
    private String type;

    @JsonProperty("site_admin")
    private boolean siteAdmin;
}
