package com.hellojenkins.app.slack.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SlackNofication {

    @JsonProperty("username")
    private String username;

    @JsonProperty("icon_emoji")
    private String iconEmoji;

    @JsonProperty("blocks")
    private List<Block> blocks;

    public SlackNofication() {}

    // --- Getters / Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getIconEmoji() { return iconEmoji; }
    public void setIconEmoji(String iconEmoji) { this.iconEmoji = iconEmoji; }

    public List<Block> getBlocks() { return blocks; }
    public void setBlocks(List<Block> blocks) { this.blocks = blocks; }

    // ==============================
    // 1️⃣ Block Interface (다형성)
    // ==============================
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.PROPERTY,
        property = "type"
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = SectionBlock.class, name = "section"),
        @JsonSubTypes.Type(value = ContextBlock.class, name = "context"),
        @JsonSubTypes.Type(value = DividerBlock.class, name = "divider")
    })
    public interface Block {}

    // ==============================
    // 2️⃣ Section Block
    // ==============================
    @JsonInclude(Include.NON_NULL)
    public static class SectionBlock implements Block {
        private final String type = "section";

        @JsonProperty("text")
        private Text text;

        @JsonProperty("fields")
        private List<Field> fields;

        @JsonProperty("accessory")
        private Accessory accessory; // ✅ Slack 버튼 지원용

        public SectionBlock() {}

        public SectionBlock(Text text, Accessory accessory) {
            this.text = text;
            this.accessory = accessory;
        }

        // --- Getters / Setters ---
        public Text getText() { return text; }
        public void setText(Text text) { this.text = text; }

        public List<Field> getFields() { return fields; }
        public void setFields(List<Field> fields) { this.fields = fields; }

        public Accessory getAccessory() { return accessory; }
        public void setAccessory(Accessory accessory) { this.accessory = accessory; }

        @JsonIgnore public String getType() { return type; }
    }

    // ==============================
    // 3️⃣ Context Block
    // ==============================
    @JsonInclude(Include.NON_NULL)
    public static class ContextBlock implements Block {
        private final String type = "context";

        @JsonProperty("elements")
        private List<Text> elements;

        public ContextBlock() {}

        public List<Text> getElements() { return elements; }
        public void setElements(List<Text> elements) { this.elements = elements; }

        @JsonIgnore public String getType() { return type; }
    }

    // ==============================
    // 4️⃣ Divider Block
    // ==============================
    @JsonSerialize
    @JsonInclude(Include.NON_NULL)
    public static class DividerBlock implements Block {
        private final String type = "divider";
        public DividerBlock() {}
        @JsonIgnore public String getType() { return type; }
    }

    // ==============================
    // 5️⃣ Text 객체
    // ==============================
    @JsonInclude(Include.NON_NULL)
    public static class Text {
        @JsonProperty("type")
        private String type;

        @JsonProperty("text")
        private String text;

        @JsonProperty("emoji")
        private Boolean emoji;

        public Text() {}
        public Text(String type, String text) {
            this.type = type;
            this.text = text;
        }

        // --- Getters / Setters ---
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public Boolean getEmoji() { return emoji; }
        public void setEmoji(Boolean emoji) { this.emoji = emoji; }
    }

    // ==============================
    // 6️⃣ Accessory 객체 (버튼용)
    // ==============================
    @JsonInclude(Include.NON_NULL)
    public static class Accessory {
        private String type = "button";

        @JsonProperty("text")
        private Text text;

        @JsonProperty("url")
        private String url;

        @JsonProperty("style")
        private String style; // optional: "primary" | "danger" 등

        public Accessory() {}

        public Accessory(Text text, String url) {
            this.text = text;
            this.url = url;
        }

        // --- Getters / Setters ---
        public Text getText() { return text; }
        public void setText(Text text) { this.text = text; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getStyle() { return style; }
        public void setStyle(String style) { this.style = style; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // ==============================
    // 7️⃣ Field 객체
    // ==============================
    @JsonInclude(Include.NON_NULL)
    public static class Field {
        @JsonProperty("type")
        private String type;

        @JsonProperty("text")
        private String text;

        public Field() {}
        public Field(String type, String text) {
            this.type = type;
            this.text = text;
        }

        // --- Getters / Setters ---
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}