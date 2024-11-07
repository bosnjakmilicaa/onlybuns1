package com.project.onlybuns.DTO;

public class RegisteredUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private int postsCount;
    private int followersCount;

    public RegisteredUserDTO(String firstName, String lastName, String email, int postsCount, int followersCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postsCount = postsCount;
        this.followersCount = followersCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    // Setteri
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    // Getteri i setteri
}