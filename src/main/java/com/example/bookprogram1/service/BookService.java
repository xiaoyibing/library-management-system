package com.example.bookprogram1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprogram1.entity.Book;

import java.util.List;

public interface BookService extends IService<Book> {
    /**
     * 获取图书列表，包含分类名称
     */
    List<Book> getListWithCategoryName();

    /**
     * 根据分类ID获取图书列表
     */
    List<Book> getListByCategoryId(Long categoryId);
}