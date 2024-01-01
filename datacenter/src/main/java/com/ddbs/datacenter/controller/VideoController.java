package com.ddbs.datacenter.controller;

import com.ddbs.datacenter.entities.Article;
import com.ddbs.datacenter.repository.db1.ArticleOneRepository;
import com.ddbs.datacenter.repository.db2.ArticleTwoRepository;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/article-videos")
public class VideoController {

    @Autowired
    ArticleOneRepository articleOneRepository;
    @Autowired
    ArticleTwoRepository articleTwoRepository;

    @GetMapping(value = "/{articleId}", produces = "video/x-flv")
    public ResponseEntity<Resource> getArticleVideo(@PathVariable String articleId, @RequestHeader HttpHeaders headers) {
        try {
            Path videoPath = getVideoPathForArticle(articleId);
            if (videoPath == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            FileSystem fs = getFileSystem();
            Resource videoResource = new InputStreamResource(fs.open(videoPath));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType("video/x-flv"))
                    .body(videoResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private FileSystem getFileSystem() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://namenode:9000");
        return FileSystem.get(configuration);
    }

    private Path getVideoPathForArticle(String articleId) {
        Article a1 = articleOneRepository.findById(articleId).orElse(null);
        Article a2 = articleTwoRepository.findById(articleId).orElse(null);
        if (a1 == null && a2 == null) return null;
        Article article = a1 == null ? a2 : a1;
        if (article.getVideo().isEmpty()) return null;
        return new Path("/articles/article" + articleId + "/" + article.getVideo());
    }
}
