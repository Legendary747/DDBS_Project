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
}
