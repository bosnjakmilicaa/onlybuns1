package com.project.onlybuns.DTO;

import java.util.List;



public class RegisteredUserDTONadja {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String address;
    private int followersCount;
    private int followingCount;
    private List<SimpleUserDTO> followingList;
    private List<SimpleUserDTO> followersList;

    public RegisteredUserDTONadja(String firstName, String lastName, String username, String email, String address,
                                  int followersCount, int followingCount,
                                  List<SimpleUserDTO> followingList, List<SimpleUserDTO> followersList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.address = address;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.followingList = followingList;
        this.followersList = followersList;
    }

    // Getteri i setteri
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

    public List<SimpleUserDTO> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<SimpleUserDTO> followingList) {
        this.followingList = followingList;
    }

    public List<SimpleUserDTO> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<SimpleUserDTO> followersList) {
        this.followersList = followersList;
    }
}
