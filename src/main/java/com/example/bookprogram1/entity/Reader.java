package com.example.bookprogram1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reader")
public class Reader {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String cardNo;
    
    private String name;
    
    private String gender;
    
    private String phone;
    
    private String email;
    
    private String address;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 非数据库字段：借阅数量
    @TableField(exist = false)
    private Integer borrowCount;
}
