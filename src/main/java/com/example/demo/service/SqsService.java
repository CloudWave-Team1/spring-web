package com.example.demo.service;

import com.example.demo.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SqsService {

    @Autowired
    private SqsClient sqsClient;

    private static final String QUEUE_URL = "https://sqs.eu-central-1.amazonaws.com/967724518132/enqueue.fifo";

    public void sendOrderMessage(String orderNumber) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .messageBody(orderNumber)
                .messageGroupId("OrderGroup")
                .build();

        sqsClient.sendMessage(sendMsgRequest);
    }

    public List<MessageDto> receiveMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        return messages.stream()
                .map(message -> new MessageDto(message.messageId(), message.body()), messages.receiptHandle())
                .collect(Collectors.toList());
    }

    public void deleteMessage(String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(deleteMessageRequest);
    }
}
