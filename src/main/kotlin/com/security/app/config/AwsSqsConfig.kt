package com.security.app.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsyncClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsSqsConfig {
    private val accessKey: String? = System.getenv("AWS_ACCESS_KEY")

    private val accessSecret: String? = System.getenv("AWS_SECRET_KEY")

    private val region: String? = System.getenv("AWS_REGION")

    @Bean
    fun amazonSQSAsync(): AmazonSQS {
        return AmazonSQSAsyncClient.builder()
            .withRegion(region)
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(accessKey, accessSecret)
                )
            )
            .build()
    }
}