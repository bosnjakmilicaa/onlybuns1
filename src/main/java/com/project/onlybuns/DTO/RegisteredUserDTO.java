package com.project.onlybuns.DTO;

public class RegisteredUserDTO {
    private String firstName;
    private String lastName;
    private String email;

    private  String username;
    private int postsCount;
    private int followersCount;

    private int followingCount;
    private String confirmPassword;

    public RegisteredUserDTO(String firstName, String lastName, String email, int postsCount, int followersCount, int followingCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postsCount = postsCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;

    }

    public RegisteredUserDTO(String firstName, String lastName, String email,String username, int postsCount, int followersCount, int followingCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.postsCount = postsCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;

    }

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

    public String getUsername() {
        return username;
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
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }


    // Getteri i setteri
}