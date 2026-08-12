package com.example.bookprogram1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprogram1.entity.BookShelfRelation;
import com.example.bookprogram1.mapper.BookShelfRelationMapper;
import com.example.bookprogram1.service.BookShelfRelationService;
import org.springframework.stereotype.Service;

@Service
public class BookShelfRelationServiceImpl extends ServiceImpl<BookShelfRelationMapper, BookShelfRelation> implements BookShelfRelationService {
}