package com.hellojenkins.app.slack.dto;

import java.util.List;

import com.hellojenkins.app.slack.dto.SlackNofication.Block;

public class SlackNotificationSettings {
	protected String buttonType;
	protected String buttonText;
	protected boolean buttonEmoji;
	
	protected String buttonAccessoryText;
	protected String buttonAccessoryUrl;
	
	protected String sectionTextType;
	protected String sectionTextText;
	
	protected SlackNofication.Text commitSectionText;
	protected SlackNofication.SectionBlock commitSectionAccessory;
	
	protected String contextElementType;
	protected String contextElementText;
	
	protected SlackNofication.ContextBlock contextBlock;
	
	protected String userName;
	
	protected String iconEmoji;
	
	protected List<Block> blocks;

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public boolean getButtonEmoji() {
		return buttonEmoji;
	}

	public void setButtonEmoji(boolean buttonEmoji) {
		this.buttonEmoji = buttonEmoji;
	}

	public String getButtonAccessoryText() {
		return buttonAccessoryText;
	}

	public void setButtonAccessoryText(String buttonAccessoryText) {
		this.buttonAccessoryText = buttonAccessoryText;
	}

	public String getButtonAccessoryUrl() {
		return buttonAccessoryUrl;
	}

	public void setButtonAccessoryUrl(String buttonAccessoryUrl) {
		this.buttonAccessoryUrl = buttonAccessoryUrl;
	}

	public String getSectionTextType() {
		return sectionTextType;
	}

	public void setSectionTextType(String sectionTextType) {
		this.sectionTextType = sectionTextType;
	}

	public String getSectionTextText() {
		return sectionTextText;
	}

	public void setSectionTextText(String sectionTextText) {
		this.sectionTextText = sectionTextText;
	}

	public SlackNofication.Text getCommitSectionText() {
		return commitSectionText;
	}

	public void setCommitSectionText(SlackNofication.Text commitSectionText) {
		this.commitSectionText = commitSectionText;
	}

	public SlackNofication.SectionBlock getCommitSectionAccessory() {
		return commitSectionAccessory;
	}

	public void setCommitSectionAccessory(SlackNofication.SectionBlock commitSectionAccessory) {
		this.commitSectionAccessory = commitSectionAccessory;
	}

	public String getContextElementType() {
		return contextElementType;
	}

	public void setContextElementType(String contextElementType) {
		this.contextElementType = contextElementType;
	}

	public String getContextElementText() {
		return contextElementText;
	}

	public void setContextElementText(String contextElementText) {
		this.contextElementText = contextElementText;
	}

	public SlackNofication.ContextBlock getContextBlock() {
		return contextBlock;
	}

	public void setContextBlock(SlackNofication.ContextBlock contextBlock) {
		this.contextBlock = contextBlock;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIconEmoji() {
		return iconEmoji;
	}

	public void setIconEmoji(String iconEmoji) {
		this.iconEmoji = iconEmoji;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}
	
	
}
