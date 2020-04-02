package org.v.th.collector.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.v.th.collector.configuration.CollectorProperties;
import org.v.th.collector.service.UploaderService;

@Component
public class EventListeners {

    private final UploaderService uploaderService;

    private final CollectorProperties properties;

    public EventListeners(@Autowired(required = false) UploaderService uploaderService, CollectorProperties properties) {
        this.uploaderService = uploaderService;
        this.properties = properties;
    }

    @Async(value = "uploaderTaskExecutor")
    @EventListener
    public void fileEventHandler(FileEvent event) {
        if (uploaderService != null) {
            uploaderService.upload(event.getFile(), properties.getUploader().getUrl(), properties.getUploader().isChecksum());
        }
    }

}
