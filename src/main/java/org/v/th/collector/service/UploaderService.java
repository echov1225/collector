package org.v.th.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.v.th.collector.dto.UploadResult;
import org.v.th.collector.exception.CollectorException;

import javax.validation.constraints.NotNull;
import java.io.File;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "collector.uploader", value = "active", matchIfMissing = true)
public class UploaderService {

    private final RestTemplate restTemplate;

    public UploaderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * upload a file
     * @param file the file to be uploaded
     * @param url upload url
     * @param isChecksum whether or not checksum
     */
    public void upload(@NotNull File file, @NotNull String url, boolean isChecksum) {
        if (log.isDebugEnabled()) {
            log.debug("uploading {}", file);
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("Parameter " + file + " does not exist");
        }
        if(!file.isFile()) {
            throw new IllegalArgumentException("Parameter " + file + " is not a file");
        }

        try {
            long checksum = isChecksum ? FileUtils.checksumCRC32(file) : 0;
            // upload file
            // set header
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.MULTIPART_FORM_DATA);
            // set body
            MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
            FileSystemResource resource = new FileSystemResource(file);
            multiValueMap.add("file", resource);
            multiValueMap.add("name", file.getName());
            multiValueMap.add("checksum", checksum);
            // send message
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multiValueMap, header);
            UploadResult result = restTemplate.postForObject(url, entity, UploadResult.class);
            if (result == null || result.getCode() != 0) {
                throw new CollectorException("upload failed: " + result);
            }

            // delete file
            FileUtils.deleteQuietly(file);
            if (log.isDebugEnabled()) {
                log.debug("{} upload successful", file);
            }
        } catch (Throwable t) {
            throw new CollectorException(t);
        }
    }

}
