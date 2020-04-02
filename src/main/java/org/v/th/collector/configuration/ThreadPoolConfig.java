package org.v.th.collector.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    @ConditionalOnProperty(prefix = "collector.uploader", value = "active", matchIfMissing = true)
    public TaskExecutor uploaderTaskExecutor(CollectorProperties properties) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix(properties.getUploader().getExecutor().getName());
        threadPoolTaskExecutor.setCorePoolSize(properties.getUploader().getExecutor().getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(properties.getUploader().getExecutor().getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(properties.getUploader().getExecutor().getQueueCapacity());
        return threadPoolTaskExecutor;
    }

}
