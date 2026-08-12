package com.example.bookprogram1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookprogram1.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
