//package com.example.demo.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AwsSqsConfig {
//    @Value("${aws.accessKeyId}")
//    private String awsAccessKey;
//
//    @Value("${aws.secretKey}")
//    private String awsSecretKey;
//
//    @Value("${aws.region}")
//    private String region;
//
//    @Bean
//    public SqsClient sqsClient() {
//        return SqsClient.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
//                .build();
//    }
//}
