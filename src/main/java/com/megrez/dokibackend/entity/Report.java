package com.megrez.dokibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 举报记录
@Data
public class Report {
    // 举报记录ID
    private Integer id;
    // 举报者ID
    private Integer reporterId;
    // 被举报视频ID
    private Integer videoId;
    // 举报原因
    private String reason;

    // 举报状态
    private enum status {
        // 待处理, 已解决, 已拒绝
        PENDING,
        RESOLVED,
        REJECTED
    }

    // 创建时间
    private LocalDateTime createdAt;
}
