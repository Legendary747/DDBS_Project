package com.ddbs.datacenter.controller;

import com.ddbs.datacenter.entities.*;
import com.ddbs.datacenter.repository.db1.*;
import com.ddbs.datacenter.repository.db2.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    UserTwoRepository userTwoRepository;
    @Autowired
    UserOneRepository userOneRepository;

    @Autowired
    ArticleTwoRepository articleTwoRepository;
    @Autowired
    ArticleOneRepository articleOneRepository;

    @Autowired
    BeReadTwoRepository beReadTwoRepository;
    @Autowired
    BeReadOneRepository beReadOneRepository;

    @Autowired
    PopularRankTwoRepository popularRankTwoRepository;
    @Autowired
    PopularRankOneRepository popularRankOneRepository;

    @Autowired
    UserReadTwoRepository userReadTwoRepository;
    @Autowired
    UserReadOneRepository userReadOneRepository;

    @GetMapping("/test")
    public String test() {
        User user1 = userOneRepository.findById("1").orElse(null);
        User user2 = userTwoRepository.findById("1").orElse(null);

        Article article1 = articleOneRepository.findById("1").orElse(null);
        Article article2 = articleTwoRepository.findById("1").orElse(null);

        UserRead userRead1 = userReadOneRepository.findById("r1").orElse(null);
        UserRead userRead2 = userReadTwoRepository.findById("r1").orElse(null);

        BeRead beRead1 = beReadOneRepository.findById("br1").orElse(null);
        BeRead beRead2 = beReadTwoRepository.findById("br1").orElse(null);

        PopularRank popularRank1 = popularRankOneRepository.findById("1").orElse(null);
        PopularRank popularRank2 = popularRankTwoRepository.findById("1").orElse(null);

        User user = user1 == null ? user2 : user1;
        Article article = article1 == null ? article2 : article1;
        UserRead userRead = userRead1 == null ? userRead2 : userRead1;
        BeRead beRead = beRead1 == null ? beRead2 : beRead1;
        PopularRank popularRank = popularRank1 == null ? popularRank2 : popularRank1;


        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(user).append("\n");
        sb.append("Article: ").append(article).append("\n");
        sb.append("UserRead: ").append(userRead).append("\n");
        sb.append("BeRead: ").append(beRead).append("\n");
        sb.append("PopularRank: ").append(popularRank).append("\n");
        return sb.toString();
    }

    @GetMapping("/article-images/{articleId}")
    public ResponseEntity<?> getArticleImages(@PathVariable String articleId) {
        try {
            List<String> imagePaths = getImagePathsForArticle(articleId); // Implement this method to get image paths
            if (imagePaths == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
            List<String> base64Images = readImagesFromHdfs(imagePaths);
            return ResponseEntity.ok(base64Images);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving images");
        }
    }

    private List<String> getImagePathsForArticle(String articleId) {
        Article a1 = articleOneRepository.findById(articleId).orElse(null);
        Article a2 = articleTwoRepository.findById(articleId).orElse(null);
        Article article = a1 == null ? a2 : a1;
        if (article == null) return null;
        String[] imageInfo = article.getImage().split(",");
        String aId = article.getId();
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < imageInfo.length; i++) {
            paths.add("/articles/article" + aId.replace("a","") + "/" + imageInfo[i]);
        }
        return paths;
    }


    public List<String> readImagesFromHdfs(List<String> imagePaths) throws IOException {
        List<byte[]> images = new ArrayList<>();
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://namenode:9000");
        FileSystem fileSystem = FileSystem.get(configuration);

        for (String imagePath : imagePaths) {
            System.out.println(imagePath);
            Path path = new Path(imagePath);
            try (FSDataInputStream inputStream = fileSystem.open(path);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                images.add(outputStream.toByteArray());
            }
        }
        List<String> base64Images = new ArrayList<>();
        for (byte[] imageBytes : images) {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            base64Images.add(base64Image);
        }
        return base64Images;
    }
}