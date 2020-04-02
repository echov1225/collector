package org.v.th.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.v.th.collector.event.EventPublisher;
import org.v.th.collector.event.FileEvent;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Service
@Slf4j
public class ScannerService {

    private final EventPublisher publisher;

    public ScannerService(EventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Finds files within a given directory
     * @param directory the directory to search in
     * @return a collection of java.io.File with the matching files
     */
    public Collection<?> scanDirectory(final File directory) {
        return FileUtils.listFiles(directory, null, true);
    }

    /**
     * Finds files within a given directory
     * @param directory the directory to search in
     * @param extensions an array of extensions, ex. {"java","xml"}. If this
     *                   parameter is {@code null}, all files are returned.
     * @param recursive  if true all subdirectories are searched as well
     * @return a collection of java.io.File with the matching files
     */
    public Collection<?> scanDirectory(final File directory, final String[] extensions, final boolean recursive) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(
                    "Parameter " + directory + " is not a directory");
        }
        return FileUtils.listFiles(directory, extensions, recursive);
    }

    /**
     * Moves a file to a directory and triggers a upload event.
     * @param source the file to be moved
     * @param dest the destination directory
     * @param createDestDir If {@code true} create the destination directory,
     *      otherwise if {@code false} throw an IOException
     */
    public void moveFileToDirectory(File source, File dest, boolean createDestDir) {
        if(!source.exists()) {
            if (log.isWarnEnabled()) {
                log.warn("{} not exists!!!", source);
            }
            return;
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug("move {} to {}", source, dest);
            }
            FileUtils.moveFileToDirectory(source, dest, createDestDir);
            publisher.publish(new FileEvent(new File(dest, source.getName())));
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
