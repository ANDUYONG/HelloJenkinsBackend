package com.hellojenkins.app.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hellojenkins.app.github.dto.branch.Protection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitBranchDTO {
	@JsonProperty("name")
	private String name;
	@JsonProperty("commit")
	private Commit commit;
	@JsonProperty("protected")
	private boolean isProtected;
	@JsonProperty("protection")
	private Protection protection;
	@JsonProperty("protection_url")
	private String protectionUrl;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Commit getCommit() {
		return commit;
	}
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	public boolean isProtected() {
		return isProtected;
	}
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}
	public Protection getProtection() {
		return protection;
	}
	public void setProtection(Protection protection) {
		this.protection = protection;
	}
	public String getProtectionUrl() {
		return protectionUrl;
	}
	public void setProtectionUrl(String protectionUrl) {
		this.protectionUrl = protectionUrl;
	}
	
	
}
