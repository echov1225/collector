package org.v.th.collector.event;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.v.th.collector.configuration.CollectorProperties;
import org.v.th.collector.exception.CollectorException;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Setter(onMethod_ = @Autowired)
    private CollectorProperties properties;

    @Setter(onMethod_ = @Autowired)
    private EventPublisher publisher;

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        if (log.isErrorEnabled()) {
            log.error(throwable.getMessage(), throwable);
        }
        // upload file event method
        if ("fileEventHandler".equals(method.getName())) {
           if (throwable instanceof CollectorException && objects[0] instanceof FileEvent) {
               FileEvent fe = (FileEvent) objects[0];
               if (properties.getUploader().getMaxRetry() != -1
                       && properties.getUploader().getMaxRetry() <= fe.getRetry()) {
                   if (log.isErrorEnabled()) {
                       log.error("{}'s number of upload failures reaches the maximum {}, upload is terminated.",
                               fe.getFile(), properties.getUploader().getMaxRetry() + 1);
                   }
                   return;
               }
               long delay = (fe.getRetry() + 1) * properties.getUploader().getIntervalBetweenUploadFailure();
               if (log.isErrorEnabled()) {
                   log.error("{} upload error {} times, delay {} seconds to upload again", fe.getFile(), fe.getRetry() + 1, delay);
               }
               // Retry upload
               FileEvent event = new FileEvent(fe.getFile(), fe.getRetry() + 1);
               ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
               ScheduledFuture<?> future = executor.schedule(() -> publisher.publish(event), delay, TimeUnit.SECONDS);
               if (future.isDone()) {
                   executor.shutdown();
               }
           }
        }
    }

}
