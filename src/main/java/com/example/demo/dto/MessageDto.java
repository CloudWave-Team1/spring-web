package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String messageId;
    private String body;
    private String receiptHandle;

    public MessageDto(String messageId, String body) {
        this.messageId = messageId;
        this.body = body;
    }

    public MessageDto(String messageId, String body, String receiptHandle) {
        this.messageId = messageId;
        this.body = body;
        this.receiptHandle = receiptHandle;
    }
}
