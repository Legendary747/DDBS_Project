package com.ddbs.datacenter.entities;

import jakarta.persistence.Column;
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
@Table(name = "article")
public class Article {

    @Id
    private String aid;
    private String timestamp;
    private String id;
    private String title;
    private String category;
    @Column(name = "abstract")
    private String abstractText;
    private String articleTags;
    private String authors;
    private String language;
    private String text;
    private String image;
    private String video;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getAid() {
        return aid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getArticleTags() {
        return articleTags;
    }

    public String getAuthors() {
        return authors;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Article{" +
                "aid='" + aid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", abstractText='" + abstractText + '\'' +
                ", articleTags='" + articleTags + '\'' +
                ", authors='" + authors + '\'' +
                ", language='" + language + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
