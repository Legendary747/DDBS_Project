package com.ddbs.datacenter.controller;

import com.ddbs.datacenter.entities.*;
import com.ddbs.datacenter.repository.db1.*;
import com.ddbs.datacenter.repository.db2.*;
import com.ddbs.datacenter.repository.db3.UserThreeRepository;
import com.ddbs.datacenter.repository.db4.UserFourRepository;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    UserThreeRepository userThreeRepository;
    @Autowired
    UserFourRepository userFourRepository;

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
    public ResponseEntity<Map<String, Object>> getTopArticles() {
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
            Article
                    article = a1 == null ? a2 : a1;
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
        Article article = getArticleCached(articleId);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @Cacheable(value = "articles", key = "#articleId")
    public Article getArticleCached(String articleId) {
        Article a1 = articleOneRepository.findById(articleId).orElse(null);
        Article a2 = articleTwoRepository.findById(articleId).orElse(null);
        return a1 == null ? a2 : a1;
    }


    @GetMapping("/be-read/{articleId}")
    public ResponseEntity<BeRead> getBeRead(@PathVariable String articleId) {
        BeRead beRead = getBeReadCached(articleId);
        if (beRead == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(beRead);
    }

    @Cacheable(value = "beReads", key = "#articleId")
    public BeRead getBeReadCached(String articleId) {
        BeRead beRead1 = beReadOneRepository.findByAid(articleId);
        System.out.println(beRead1);
        BeRead beRead2 = beReadTwoRepository.findByAid(articleId);
        System.out.println(beRead2);
        return beRead1 == null ? beRead2 : beRead1;
    }

    @GetMapping("/article-images/{articleId}")
    public ResponseEntity<List<String>> getArticleImages(@PathVariable String articleId) {
        List<String> base64Images = getArticleImagesCached(articleId);
        if (base64Images == null || base64Images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList("Article not found or no images available"));
        }
        return ResponseEntity.ok(base64Images);
    }

    @Cacheable(value = "articleImages", key = "#articleId")
    public List<String> getArticleImagesCached(String articleId) {
        try {
            List<String> imagePaths = getImagePathsForArticle(articleId);
            if (imagePaths == null) return null;
            return readImagesFromHdfs(imagePaths);
        } catch (IOException e) {
            // Log the exception and return null or an empty list
            return null;
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = getUserCached(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Cacheable(value = "users", key = "#userId")
    public User getUserCached(String userId) {
        User u1 = userOneRepository.findById(userId).orElse(null);
        User u2 = userTwoRepository.findById(userId).orElse(null);
        return u1 == null ? u2 : u1;
    }

    @GetMapping("/user-read/{userId}")
    public ResponseEntity<List<String>> getUserRead(@PathVariable String userId) {
        List<String> readArticleIds = getUserReadCached(userId);
        return ResponseEntity.ok(readArticleIds);
    }

    @Cacheable(value = "userReads", key = "#userId")
    public List<String> getUserReadCached(String userId) {
        List<UserRead> userRead1 = userReadOneRepository.findByUid(userId);
        List<UserRead> userRead2 = userReadTwoRepository.findByUid(userId);
        List<UserRead> userRead = new ArrayList<>();
        userRead.addAll(userRead1);
        userRead.addAll(userRead2);
        return userRead.stream().map(UserRead::getAid).toList();
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

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User userData) {
        System.out.println(userData);
        // Get the max uid and generate new uid
        Integer maxUidOne = Integer.parseInt(userOneRepository.findMaxUid());
        Integer maxUidTwo = Integer.parseInt(userTwoRepository.findMaxUid());
        Integer maxUid = maxUidOne > maxUidTwo ? maxUidOne : maxUidTwo;
        String nextUid = "" + (maxUid + 1);
        String nexId = "u" + nextUid;

        // Create new User object
        User newUser = new User();
        newUser.setUid(nextUid);
        newUser.setId(nexId.substring(0,5)); // Assuming id is a string representation of nextUid
        newUser.setTimestamp(String.valueOf(System.currentTimeMillis()));
        newUser.setName(userData.getName());
        newUser.setGender(userData.getGender());
        newUser.setEmail(userData.getEmail());
        newUser.setPhone(userData.getPhone());
        newUser.setDept(userData.getDept());
        newUser.setGrade(userData.getGrade());
        newUser.setLanguage(userData.getLanguage());
        newUser.setRegion(userData.getRegion());
        newUser.setRole(userData.getRole());
        newUser.setPreferTags(userData.getPreferTags());
        newUser.setObtainedCredits(userData.getObtainedCredits());
        System.out.println(newUser);
        if (newUser.getRegion().equals("Beijing")) {
            userOneRepository.save(newUser);
            userThreeRepository.save(newUser);
        } else {
            userTwoRepository.save(newUser);
            userFourRepository.save(newUser);
        }
        return ResponseEntity.ok(newUser);
    }

}
