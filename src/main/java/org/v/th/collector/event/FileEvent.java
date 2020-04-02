package org.v.th.collector.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.File;

public class FileEvent extends ApplicationEvent {

    @Getter
    private File file;

    @Getter
    private int retry;

    public FileEvent(File file) {
        this(file, 0);
    }

    public FileEvent(File file, int retry) {
        super(file);
        this.file = file;
        this.retry = retry;
    }
}
