package com.example.bookprogram1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String isbn;
    
    private String name;
    
    private String author;
    
    private Long categoryId;
    
    private String publisher;
    
    private LocalDate publishDate;
    
    private BigDecimal price;
    
    private Integer stock;
    
    private Integer available;
    
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 非数据库字段
    @TableField(exist = false)
    private String categoryName;
}
