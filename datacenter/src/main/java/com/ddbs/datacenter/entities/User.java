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
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    private String uid;
    private String timestamp;
    private String id;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String dept;
    private String grade;
    private String language;
    private String region;
    private String role;

    @Column(name = "preferTags")
    private String preferTags;

    @Column(name = "obtainedCredits")
    private String obtainedCredits;

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dept='" + dept + '\'' +
                ", grade='" + grade + '\'' +
                ", language='" + language + '\'' +
                ", region='" + region + '\'' +
                ", role='" + role + '\'' +
                ", preferTags='" + preferTags + '\'' +
                ", obtainedCredits='" + obtainedCredits + '\'' +
                '}';
    }
}
