package com.ddbs.datacenter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "article")
public class Article {

    @Id
    private String aid;
    private String timestamp;
    private String id;
    private String title;
    private String category;
    private String abstractText;
    private String articleTags;
    private String authors;
    private String language;
    private String text;
    private String image;
    private String video;
}
