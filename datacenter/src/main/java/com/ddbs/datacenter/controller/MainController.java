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
import java.util.*;

@RestController
public class MainController {

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

    public String[] returnArticleIds(String articleIds) {
        String[] ids = articleIds
                .replace("{","")
                .replace("}","")
                .split(", ");
        return ids;
    }
    @GetMapping("/top_articles")
    public ResponseEntity<?> getTopArticles() {
        // day
        List<PopularRank> popularRanks1 = popularRankOneRepository.findAll();
        PopularRank popularRankDay = popularRanks1.get(0);
        System.out.println(popularRankDay);
        // week, month
        List<PopularRank> popularRanks2 = popularRankTwoRepository.findAll();
        PopularRank popularRankWeek = popularRanks2.get(0);
        PopularRank popularRankMonth = popularRanks2.get(1);

        String[] dailyId = returnArticleIds(popularRankDay.getArticleIds());
        String[] weeklyId = returnArticleIds(popularRankWeek.getArticleIds());
        String[] monthlyId = returnArticleIds(popularRankMonth.getArticleIds());

        List<Article> dailyArticles = new ArrayList<>();
        List<Article> weeklyArticles = new ArrayList<>();
        List<Article> monthlyArticles = new ArrayList<>();

        for (String id : dailyId) {
            System.out.println(id);
            Article a1 = articleOneRepository.findById(id).orElse(null);
            Article a2 = articleTwoRepository.findById(id).orElse(null);
            Article article = a1 == null ? a2 : a1;
            System.out.println(article);
            if (article != null) dailyArticles.add(article);
        }
        for (String id : weeklyId) {
            Article a1 = articleOneRepository.findById(id).orElse(null);
            Article a2 = articleTwoRepository.findById(id).orElse(null);
            Article article = a1 == null ? a2 : a1;
            if (article != null) weeklyArticles.add(article);
        }
        for (String id : monthlyId) {
            Article a1 = articleOneRepository.findById(id).orElse(null);
            Article a2 = articleTwoRepository.findById(id).orElse(null);
            Article article = a1 == null ? a2 : a1;
            if (article != null) monthlyArticles.add(article);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("daily", dailyArticles);
        response.put("weekly", weeklyArticles);
        response.put("monthly", monthlyArticles);

        return ResponseEntity.ok(response);
    }


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

    @GetMapping("/article/{articleId}")
    public ResponseEntity<Article> getArticle(@PathVariable String articleId) {
        Article a1 = articleOneRepository.findById(articleId).orElse(null);
        Article a2 = articleTwoRepository.findById(articleId).orElse(null);
        Article article = a1 == null ? a2 : a1;
        System.out.println(article);
        if (article == null) return null;
        return ResponseEntity.ok(article);
    }

    @GetMapping("/be-read/{articleId}")
    public ResponseEntity<BeRead> getBeRead(@PathVariable String articleId) {
        BeRead beRead1 = beReadOneRepository.findByAid(articleId);
        System.out.println(beRead1);
        BeRead beRead2 = beReadTwoRepository.findByAid(articleId);
        System.out.println(beRead2);
        BeRead beRead = beRead1 == null ? beRead2 : beRead1;

        return ResponseEntity.ok(beRead);
    }

    @GetMapping("/article-images/{articleId}")
    public ResponseEntity<?> getArticleImages(@PathVariable String articleId) {
        try {
            List<String> imagePaths = getImagePathsForArticle(articleId); // Implement this method to get image paths
            if (imagePaths == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
            System.out.println(imagePaths);
            List<String> base64Images = readImagesFromHdfs(imagePaths);
            return ResponseEntity.ok(base64Images);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving images");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User u1 = userOneRepository.findById(userId).orElse(null);
        User u2 = userTwoRepository.findById(userId).orElse(null);
        User user = u1 == null ? u2 : u1;
        if (user == null) return null;
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user-read/{userId}")
    public ResponseEntity<List<String>> getUserRead(@PathVariable String userId) {
        List<UserRead> userRead1 = userReadOneRepository.findByUid(userId);
        List<UserRead> userRead2 = userReadTwoRepository.findByUid(userId);
        List<UserRead> userRead = new ArrayList<>();
        userRead.addAll(userRead1);
        userRead.addAll(userRead2);
        List<String> readArticleIds = userRead.stream().map(UserRead::getAid).toList();

        return ResponseEntity.ok(readArticleIds);
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
