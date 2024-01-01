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
@Table(name = "user_read")
public class UserRead {

    @Id
    private String id;
    private String timestamp;
    private String uid;
    private String aid;
    private String readTimeLength;
    private String agreeOrNot;
    private String commentOrNot;
    private String shareOrNot;
    private String commentDetail;

    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getAid() {
        return aid;
    }

    public String getReadTimeLength() {
        return readTimeLength;
    }

    public String getAgreeOrNot() {
        return agreeOrNot;
    }

    public String getCommentOrNot() {
        return commentOrNot;
    }

    public String getShareOrNot() {
        return shareOrNot;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    @Override
    public String toString() {
        return "UserRead{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", uid='" + uid + '\'' +
                ", aid='" + aid + '\'' +
                ", readTimeLength='" + readTimeLength + '\'' +
                ", agreeOrNot='" + agreeOrNot + '\'' +
                ", commentOrNot='" + commentOrNot + '\'' +
                ", shareOrNot='" + shareOrNot + '\'' +
                ", commentDetail='" + commentDetail + '\'' +
                '}';
    }
}
