package com.ddbs.datacenter.repository.db1;

import com.ddbs.datacenter.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleOneRepository extends JpaRepository<Article, String> {
    // Custom query methods for Article
}
