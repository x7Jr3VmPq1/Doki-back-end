package com.megrez.dokibackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoDTO {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Integer duration;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer likeNumber;
    private Integer commentNumber;
    private Integer favorites;
}
