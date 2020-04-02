package org.v.th.collector.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.v.th.collector.configuration.CollectorProperties;
import org.v.th.collector.service.ScannerService;

import java.io.File;
import java.util.Collection;

@Slf4j
public class ScannerScheduler {

    @Autowired
    private CollectorProperties collectorProperties;

    @Autowired
    private ScannerService scannerService;

    @Scheduled(cron = "${collector.scanner.corn}")
    public void scan() {
        if (log.isInfoEnabled()) {
            log.info("Start scanning {}", collectorProperties.getScanner().getPath());
        }
        // list files
        Collection<?> files = scannerService.scanDirectory(collectorProperties.getScanner().getPath());
        if (log.isInfoEnabled()) {
            log.info("There are {} files", files.size());
        }
        // Moves file to the folder to be uploaded
        files.forEach(f -> scannerService.moveFileToDirectory((File) f, collectorProperties.getScanner().getTemp(), true));
        if (log.isInfoEnabled()) {
            log.info("End scanning {}", collectorProperties.getScanner().getPath());
        }
    }

}
