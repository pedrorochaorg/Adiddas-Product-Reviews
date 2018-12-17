package com.adidas.products.reviews.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Class that customizes the thread pool used by the async task executor.
 *
 * @author pedrorocha
 **/
@Configuration
public class TaskExecutors {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("CalculateProductScore-");
        executor.initialize();
        return executor;
    }
}
