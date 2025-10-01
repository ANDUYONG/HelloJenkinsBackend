package com.hellojenkins.app.github.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Verification {
	@JsonProperty("verified")
    private boolean verified;
	@JsonProperty("reason")
    private String reason;
	@JsonProperty("signature")
    private String signature;
	@JsonProperty("payload")
    private String payload;
}
