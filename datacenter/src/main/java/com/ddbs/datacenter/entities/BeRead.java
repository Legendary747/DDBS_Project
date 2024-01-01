package com.ddbs.datacenter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "be_read")
public class BeRead {

    @Id
    private String id;
    private String timestamp;
    private String aid;
    private String readNum;
    private String readUidList;
    private String commentNum;
    private String commentUidList;
    private String agreeNum;
    private String agreeUidList;
    private String shareNum;
    private String shareUidList;

    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAid() {
        return aid;
    }

    public String getReadNum() {
        return readNum;
    }

    public String getReadUidList() {
        return readUidList;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public String getCommentUidList() {
        return commentUidList;
    }

    public String getAgreeNum() {
        return agreeNum;
    }

    public String getAgreeUidList() {
        return agreeUidList;
    }

    public String getShareNum() {
        return shareNum;
    }

    public String getShareUidList() {
        return shareUidList;
    }

    @Override
    public String toString() {
        return "BeRead{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", aid='" + aid + '\'' +
                ", readNum='" + readNum + '\'' +
                ", readUidList='" + readUidList + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", commentUidList='" + commentUidList + '\'' +
                ", agreeNum='" + agreeNum + '\'' +
                ", agreeUidList='" + agreeUidList + '\'' +
                ", shareNum='" + shareNum + '\'' +
                ", shareUidList='" + shareUidList + '\'' +
                '}';
    }
}
