package com.dev.doculens.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {

    @Bean("DocUploadThreadPool")
    @Primary
    public Executor docuUploadExecutor() {
        int minPoolSize = 30;
        int maxPoolSize = 50;
        int queueSize = 200;

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(minPoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueSize);
        taskExecutor.setThreadNamePrefix("Document Upload Worker thread ");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
