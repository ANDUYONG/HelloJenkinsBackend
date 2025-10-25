package com.hellojenkins.app.slack;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellojenkins.app.slack.dto.SlackNofication;
import com.hellojenkins.app.slack.dto.SlackNotificationSettings;

@Service
public class SlackService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${slack.webhook-url}")
    private String slackWebhookUrl;

    @Value("${jenkins.base-url}")
    private String jenkinsBaseUrl;

    /**
     * Jenkins 빌드 결과를 Slack으로 전송
     */
    public void sendFromJenkins(String jobName, String branchName, int buildNumber, String status) {
        try {
            String statusEmoji = status.equalsIgnoreCase("SUCCESS") || status.equalsIgnoreCase("COMPLETED") ? ":white_check_mark:" : ":x:";

            // 브랜치명 인코딩
//            String encodedBranchName = URLEncoder.encode(branchName, StandardCharsets.UTF_8.toString())
//                    .replace("+", "%20");

            // Jenkins 빌드 로그 URL 구성
            String buildUrl = String.format(
                    "http://localhost:%s/api/jenkins/overview/%s/total?branchName=%s",
                    branchName.equals("main") ? "80" : "8081",
                    buildNumber,
                    branchName
            );

            SlackNotificationSettings settings = new SlackNotificationSettings() {{
                this.buttonType = "plain_text";
                this.buttonText = "빌드 로그 보기";
                this.buttonEmoji = true;
                this.buttonAccessoryUrl = buildUrl;
                this.sectionTextType = "mrkdwn";
                this.sectionTextText = String.format("*Jenkins 빌드 알림*: %s `%s`", statusEmoji, branchName);
                this.contextElementType = "mrkdwn";
                this.contextElementText = String.format("상태: *%s* | <@U09MZBHMMC4>에서 확인하세요.", status);
                this.userName = "Jenkins Bot";
                this.iconEmoji = ":jenkins:";
            }};

            SlackNofication payload = setNotification(settings);
            postToSlack(slackWebhookUrl, payload);

        } catch (Exception e) {
            System.err.println("URL 인코딩 오류: " + e.getMessage());
        }
    }

    /**
     * GitHub 커밋 정보를 Slack으로 전송
     */
    public void sendFromGithub(String repoName, String author, String commitMessage, String commitUrl, String branchName) {
        String contextText = String.format(
                ":branch: *브랜치:* `%s` | :bust_in_silhouette: *작성자:* %s",
                branchName,
                author
        );

        SlackNotificationSettings settings = new SlackNotificationSettings() {{
            this.buttonType = "plain_text";
            this.buttonText = "커밋 보기";
            this.buttonEmoji = true;
            this.buttonAccessoryUrl = commitUrl;
            this.sectionTextType = "mrkdwn";
            this.sectionTextText = String.format("*[%s]* 새로운 커밋 발생: *%s*",
                    repoName, commitMessage.split("\n")[0]);
            this.contextElementType = "mrkdwn";
            this.contextElementText = contextText;
            this.userName = author;
            this.iconEmoji = ":github:";
        }};

        SlackNofication payload = setNotification(settings);
        postToSlack(slackWebhookUrl, payload);
    }

    /**
     * Slack Notification DTO 구성
     */
    public SlackNofication setNotification(SlackNotificationSettings settings) {
        // 버튼 텍스트
        SlackNofication.Text buttonText = new SlackNofication.Text();
        buttonText.setType(settings.getButtonType());
        buttonText.setText(settings.getButtonText());
        buttonText.setEmoji(settings.getButtonEmoji());

        // ✅ 버튼 액세서리 (type 추가)
        SlackNofication.Accessory buttonAccessory = new SlackNofication.Accessory();
        buttonAccessory.setType("button"); // <-- 중요!
        buttonAccessory.setText(buttonText);
        buttonAccessory.setUrl(settings.getButtonAccessoryUrl());

        // Section Block (예: 커밋 메시지)
        SlackNofication.Text sectionText = new SlackNofication.Text();
        sectionText.setType(settings.getSectionTextType());
        sectionText.setText(settings.getSectionTextText());

        SlackNofication.SectionBlock commitSection = new SlackNofication.SectionBlock(sectionText, buttonAccessory);

        // Context Block (예: 작성자, 브랜치 정보)
        SlackNofication.Text contextElement = new SlackNofication.Text();
        contextElement.setType(settings.getContextElementType());
        contextElement.setText(settings.getContextElementText());

        SlackNofication.ContextBlock contextBlock = new SlackNofication.ContextBlock();
        contextBlock.setElements(List.of(contextElement));
        
        // 2. Slack 메시지 본문 구성
        SlackNofication dto = new SlackNofication();
        dto.setUsername(settings.getUserName());
        dto.setIconEmoji(settings.getIconEmoji());
        dto.setBlocks(List.of(
            contextBlock,
            new SlackNofication.DividerBlock(),
            commitSection
        ));
        return dto;
    }


    /**
     * Slack Webhook 호출
     */
    private void postToSlack(String webHookUrl, SlackNofication payload) {
        try {
            String jsonPayload = mapper.writeValueAsString(payload);
            System.err.println("webHookUrl: " + webHookUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            restTemplate.postForLocation(webHookUrl, entity);

            System.out.println("✅ Slack 알림 전송 완료: " + webHookUrl);
        } catch (JsonProcessingException e) {
            System.err.println("Slack 페이로드 JSON 변환 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Slack 웹훅 호출 오류: " + e.getMessage());
        }
    }
}
