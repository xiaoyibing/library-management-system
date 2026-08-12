package com.example.bookprogram1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprogram1.entity.BookShelf;
import com.example.bookprogram1.mapper.BookShelfMapper;
import com.example.bookprogram1.service.BookShelfService;
import org.springframework.stereotype.Service;

@Service
public class BookShelfServiceImpl extends ServiceImpl<BookShelfMapper, BookShelf> implements BookShelfService {
}