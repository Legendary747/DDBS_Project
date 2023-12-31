package com.ddbs.datacenter.controller;

import com.ddbs.datacenter.entities.*;
import com.ddbs.datacenter.repository.db1.*;
import com.ddbs.datacenter.repository.db2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
