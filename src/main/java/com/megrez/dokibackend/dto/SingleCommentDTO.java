package com.megrez.dokibackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SingleCommentDTO {
    // 评论id
    private Integer id;
    // 评论内容
    @NotBlank(message = "评论内容不能为空")
    private String content;
    // 评论者id
    @NotNull(message = "评论者id不能为空")
    private Integer userId;
    // 视频id
    @NotNull(message = "视频id不能为空")
    private Integer videoId;
    // 图片url(传入时以BASE64格式，后续转换为URL地址)
    private String imgUrl;
    // 父评论id
    private Integer parentCommentId;
}
