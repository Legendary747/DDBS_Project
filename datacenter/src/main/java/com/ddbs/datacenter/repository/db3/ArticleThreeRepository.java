package com.ddbs.datacenter.repository.db3;

import com.ddbs.datacenter.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleThreeRepository extends JpaRepository<Article, String> {
    // Custom query methods for Article
}
