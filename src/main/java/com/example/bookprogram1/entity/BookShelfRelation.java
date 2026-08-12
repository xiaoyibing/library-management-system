package com.example.bookprogram1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("book_shelf_relation")
public class BookShelfRelation {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;

    private Long shelfId;

    private Long categoryId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}