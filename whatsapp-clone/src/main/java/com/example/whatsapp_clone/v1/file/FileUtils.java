package com.example.whatsapp_clone.v1.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    private FileUtils() {}

    public static byte[] readFileFromLocation(String fileUrl) {
        if(StringUtils.isBlank(fileUrl)) {
            return new byte[0];
        }
        try {
            Path path = Paths.get(fileUrl);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.warn("Non file found in the path {}", fileUrl);
        }
        return new byte[0];
    }
}
