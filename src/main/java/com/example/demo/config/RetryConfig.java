package com.example.demo.config;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRetry  // Enables retry mechanism in your Spring Boot application
public class RetryConfig {
}
