package com.hellojenkins.app.github.dto.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Protection {
	@JsonProperty("enabled")
	private boolean enabled;
	@JsonProperty("required_status_checks")
	private RequiredStatusChecks requiredStatusChecks;
}
