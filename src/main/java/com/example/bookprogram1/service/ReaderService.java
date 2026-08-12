package com.example.bookprogram1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprogram1.entity.Reader;

public interface ReaderService extends IService<Reader> {
    
    Reader getByUserId(Long userId);
}
