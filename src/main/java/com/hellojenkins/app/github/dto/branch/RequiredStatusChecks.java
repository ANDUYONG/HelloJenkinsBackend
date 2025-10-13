package com.hellojenkins.app.github.dto.branch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class RequiredStatusChecks {
	@JsonProperty("enforcement_level")
	private String enforcementLevel;
	@JsonProperty("contexts")
	private List<Object> contexts;
	@JsonProperty("checks")
	private List<Object> checks;
}
