package com.megrez.dokibackend.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private long total;         // 总记录数
    private int page;           // 当前页码
    private int size;           // 每页大小
    private List<T> records;    // 当前页数据列表

    // 工厂方法：快速创建分页结果
    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        return new PageResult<>(total, page, size, records);
    }
}
