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

//    @Value("${sqs.queue.url}")
    private String queueUrl = "https://sqs.ap-northeast-2.amazonaws.com/967724518132/enqueue.fifo";

    public void sendOrderMessage(String orderNumber) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(orderNumber)
                .messageGroupId("OrderGroup")
                .build();

        sqsClient.sendMessage(sendMsgRequest);
    }

    public List<MessageDto> receiveMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        return messages.stream()
                .map(message -> new MessageDto(message.messageId(), message.body(), message.receiptHandle()))
                .collect(Collectors.toList());


    }

    public void deleteMessage(String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(deleteMessageRequest);
    }
}
