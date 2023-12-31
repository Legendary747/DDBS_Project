package com.ddbs.datacenter.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
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
    private String preferTags;
    private String obtainedCredits;

    // Getters and setters for all fields
}
