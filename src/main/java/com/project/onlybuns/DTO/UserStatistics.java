package com.project.onlybuns.DTO;

public class UserStatistics {

    private double postsPercentage;
    private double commentsOnlyPercentage;
    private double noActivityPercentage;

    public UserStatistics(double postsPercentage, double commentsOnlyPercentage, double noActivityPercentage) {
        this.postsPercentage = postsPercentage;
        this.commentsOnlyPercentage = commentsOnlyPercentage;
        this.noActivityPercentage = noActivityPercentage;
    }

    public double getPostsPercentage() {
        return postsPercentage;
    }

    public double getCommentsOnlyPercentage() {
        return commentsOnlyPercentage;
    }

    public double getNoActivityPercentage() {
        return noActivityPercentage;
    }
}
