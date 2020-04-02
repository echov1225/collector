package org.v.th.collector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.v.th.collector.configuration.CollectorProperties;
import org.v.th.collector.event.EventPublisher;
import org.v.th.collector.event.FileEvent;
import org.v.th.collector.service.ScannerService;

import java.io.File;
import java.util.Collection;

@Slf4j
@Component
public class ScannerTempApplicationRunner implements ApplicationRunner {

    private final CollectorProperties properties;

    private final ScannerService scannerService;

    private final EventPublisher publisher;

    public ScannerTempApplicationRunner(CollectorProperties properties, ScannerService scannerService, EventPublisher publisher) {
        this.properties = properties;
        this.scannerService = scannerService;
        this.publisher = publisher;
    }

    @Override
    public void run(ApplicationArguments args) {
        File temp = properties.getScanner().getTemp();
        Collection<?> files = scannerService.scanDirectory(temp);
        log.info("scan {} files in {}", files.size(), temp);
        files.forEach(file -> publisher.publish(new FileEvent((File) file)));
    }
}
