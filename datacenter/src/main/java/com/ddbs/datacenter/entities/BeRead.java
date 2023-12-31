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
}
