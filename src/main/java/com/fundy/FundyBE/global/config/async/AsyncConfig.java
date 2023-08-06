package com.fundy.FundyBE.global.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "ThreadPoolTaskExecutor")
    public Executor getTaskExecutor() {
        /*
          maxPoolSize만큼 확장되는 것보다 Queue를 채우는 작업이 우선한다. Queue의 크기를 넘어선 수의 작업들이 요청되었을 때만 maxPoolSize만큼 확장된다.
          1. corePoolSize보다 적은 Thread가 수행되고 있었던 경우: 실행 요청한 Runnable을 수행하기 위한 Thread를 새로 생성하여 즉시 실행한다.
          2. corePoolSize보다 많은 Thread가 수행되고 있지만, maxPoolSize보다 적은 수의 Thread가 수행되고 있는 경우:
               - Queue가 가득 차지 않은 경우: 즉시 실행하지 않고 Queue에 Runnable을 넣는다.
               - Queue가 가득 찬 경우: maxPoolSize까지 Thread를 더 만들어 실행한다.
         */

        int corePoolSize = 10;
        int maximumPoolSize = 30;
        int queueSize = 20;
        long keepAliveTime = 30;
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(queueSize);
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                blockingQueue
        );
    }
}
