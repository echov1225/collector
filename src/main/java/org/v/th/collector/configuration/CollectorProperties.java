package org.v.th.collector.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@Data
@ConfigurationProperties(prefix = "collector")
public class CollectorProperties {

    private final Scanner scanner = new Scanner();

    private final Uploader uploader = new Uploader();

    private final Rest rest = new Rest();

    @Data
    public static class Scanner {

        private File path;

        private String corn;

        private File temp = new File(".temp");

        private boolean active = true;
    }

    @Data
    public static class Uploader {
        private String url;

        private int intervalBetweenUploadFailure = 10;

        private int maxRetry = 10;

        private boolean checksum = true;

        private final Executor executor = new Executor();

        private boolean active = true;
    }

    @Data
    public static class Executor {
        private String name = "uploader-task";

        private int corePoolSize = 5;

        private int maxPoolSize = 10;

        private int queueCapacity = 100;
    }

    @Data
    public static class Rest {
        private int connectionTimeout = 10000;

        private int readTimeout = 60000;

        private final Proxy proxy = new Proxy();

        @Data
        public static class Proxy {
            private boolean active = false;

            private String host;

            private int port;
        }
    }

}
