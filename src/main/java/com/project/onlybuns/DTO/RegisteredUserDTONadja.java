package com.project.onlybuns.DTO;

import com.project.onlybuns.model.Follow;

import java.util.List;

public class RegisteredUserDTONadja {
    private String firstName;
    private String lastName;
    private String email;

    private  String username;
    private String address;
    private int postsCount;
    private int followersCount;

    private int followingCount;
    private String confirmPassword;

    List<Follow> followingList;
    List<Follow> followersList;

    public RegisteredUserDTONadja(String firstName, String lastName, String email, String address, int followersCount, int followingCount, List<Follow> followingList, List<Follow> followersList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.followingList = followingList;
        this.followersList = followersList;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<Follow> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<Follow> followingList) {
        this.followingList = followingList;
    }

    public List<Follow> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<Follow> followersList) {
        this.followersList = followersList;
    }
}
