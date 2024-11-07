package com.project.onlybuns.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.onlybuns.model.Comment;
import com.project.onlybuns.model.Like;
import com.project.onlybuns.model.RegisteredUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDTO {

    private String description;
    private String imageUrl;

    public PostDTO() {}

    public PostDTO(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
