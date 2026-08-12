package com.example.bookprogram1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("borrow_record")
public class BorrowRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long readerId;
    
    private Long bookId;
    
    private LocalDateTime borrowTime;
    
    private LocalDateTime dueTime;
    
    private LocalDateTime returnTime;
    
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 非数据库字段
    @TableField(exist = false)
    private String readerName;
    
    @TableField(exist = false)
    private String bookName;
    
    @TableField(exist = false)
    private String author;
}
