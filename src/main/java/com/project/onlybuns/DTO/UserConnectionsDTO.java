package com.project.onlybuns.DTO;


import java.util.List;

public class UserConnectionsDTO {
    private List<String> followers;
    private List<String> following;

    public UserConnectionsDTO(List<String> followers, List<String> following) {
        this.followers = followers;
        this.following = following;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}
