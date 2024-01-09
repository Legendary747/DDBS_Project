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

    public String getUid() {
        return uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDept() {
        return dept;
    }

    public String getGrade() {
        return grade;
    }

    public String getLanguage() {
        return language;
    }

    public String getRegion() {
        return region;
    }

    public String getRole() {
        return role;
    }

    public String getPreferTags() {
        return preferTags;
    }

    public String getObtainedCredits() {
        return obtainedCredits;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPreferTags(String preferTags) {
        this.preferTags = preferTags;
    }

    public void setObtainedCredits(String obtainedCredits) {
        this.obtainedCredits = obtainedCredits;
    }

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
