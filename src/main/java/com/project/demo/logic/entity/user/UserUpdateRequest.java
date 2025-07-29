package com.project.demo.logic.entity.user;

import org.springframework.web.multipart.MultipartFile;

public class UserUpdateRequest {

    private String name;
    private String lastname;
    private Integer age;
    private Integer points;
    private Long roleId;

    private MultipartFile image;

    public UserUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }


    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
