package com.hellojenkins.app.github.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GitTrreDTO {
	@JsonProperty("sha")
	private String sha;
	@JsonProperty("truncated")
    private boolean truncated;
	@JsonProperty("tree")
    private List<TreeNode> tree;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TreeNode {
    	@JsonProperty("path")
        private String path;
    	@JsonProperty("mode")
        private String mode;
    	@JsonProperty("type")
        private String type;
    	@JsonProperty("sha")
        private String sha;
    	@JsonProperty("size")
        private Long size; // 일부 타입에서는 size가 없을 수 있음
    	
    	private String fileName;
    	private String filePath;
    	private boolean isFile;
    	private List<TreeNode> children;
    	
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getSha() {
			return sha;
		}
		public void setSha(String sha) {
			this.sha = sha;
		}
		public Long getSize() {
			return size;
		}
		public void setSize(Long size) {
			this.size = size;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public boolean isFile() {
			return isFile;
		}
		public void setFile(boolean isFile) {
			this.isFile = isFile;
		}
		public List<TreeNode> getChildren() {
			return children;
		}
		public void setChildren(List<TreeNode> children) {
			this.children = children;
		}
    	
    	
    }

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public List<TreeNode> getTree() {
		return tree;
	}

	public void setTree(List<TreeNode> tree) {
		this.tree = tree;
	}

    
    
}
