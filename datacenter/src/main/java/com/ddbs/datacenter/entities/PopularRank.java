package com.ddbs.datacenter.entities;

import com.google.common.base.Splitter;
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
@Setter
@Table(name = "popular_rank")
public class PopularRank {

    @Id
    private String id;
    private String timestamp;
    private String temporalGranularity;
    private String articleAidList;

    @Override
    public String toString() {
        return "PopularRank{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", temporalGranularity='" + temporalGranularity + '\'' +
                ", articleAidList='" + articleAidList + '\'' +
                '}';
    }

    public String getArticleIds() {
        return this.articleAidList;
    }
}
