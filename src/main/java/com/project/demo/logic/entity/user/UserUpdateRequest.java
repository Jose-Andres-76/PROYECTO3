package com.project.demo.logic.entity.user;

import org.springframework.web.multipart.MultipartFile;

public class UserUpdateRequest {

    private String name;
    private String lastname;
    private Integer  age;

    private String password;

    private MultipartFile image;
    private String passwordConfirmation;

    public UserUpdateRequest() {};

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

    public int getAge() {
        return age;
    }

    public void setAge(Integer  age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String toString() {
        return "UserUpdateRequest{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                ", password='" + password + '\'' +
                ", image=" + image +
                '}';
    }
}
