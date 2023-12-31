package com.ddbs.datacenter.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class HdfsController {

    private final String hdfsUri = "hdfs://namenode:9000";

    @GetMapping("/hdfs/**")
    public void streamFileFromHdfs(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String filePath = extractFilePathFromRequest(request);
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUri);
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("/articles/" + filePath);
        System.out.println("PATH: " + path);

        try (InputStream inputStream = fileSystem.open(path);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("image/jpg"); // Set appropriate content type
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private String extractFilePathFromRequest(HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern("/hdfs/**", path);
    }
}
