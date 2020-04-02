package org.v.th.collector.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.v.th.collector.scheduler.ScannerScheduler;

@Configuration
@EnableConfigurationProperties(CollectorProperties.class)
public class CollectorConfig {

    @Bean
    @ConditionalOnProperty(prefix = "collector.sender", value = "active", matchIfMissing = true)
    public ScannerScheduler fileMonitoringScheduler() {
        return new ScannerScheduler();
    }

}
