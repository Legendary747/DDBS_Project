package com.ddbs.datacenter.repository.db2;

import com.ddbs.datacenter.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTwoRepository extends JpaRepository<Article, String> {
    // Custom query methods for Article
}
