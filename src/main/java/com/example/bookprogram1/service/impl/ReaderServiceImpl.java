package com.example.bookprogram1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprogram1.entity.Reader;
import com.example.bookprogram1.mapper.ReaderMapper;
import com.example.bookprogram1.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl extends ServiceImpl<ReaderMapper, Reader> implements ReaderService {

    @Override
    public Reader getByUserId(Long userId) {
        return baseMapper.findByUserId(userId);
    }
}
