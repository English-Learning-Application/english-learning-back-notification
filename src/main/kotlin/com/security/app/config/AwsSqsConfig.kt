package com.security.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Configuration
class AwsSqsConfig {
    private val accessKey: String? = System.getenv("AWS_ACCESS_KEY")

    private val accessSecret: String? = System.getenv("AWS_SECRET_KEY")

    private val region: String? = System.getenv("AWS_REGION")

    @Bean
    fun amazonSQSAsync(): SqsAsyncClient {
        return SqsAsyncClient.builder()
            .region(Region.of(region))
            .credentialsProvider {
                AwsBasicCredentials.create(accessKey, accessSecret)
            }
            .build()
    }
}