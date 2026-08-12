package com.example.bookprogram1.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprogram1.entity.BorrowRecord;

import java.util.List;
import java.util.Map;

public interface BorrowRecordService extends IService<BorrowRecord> {
    
    IPage<BorrowRecord> getPageWithDetail(Page<BorrowRecord> page, String status);
    
    IPage<BorrowRecord> getMyPageWithDetail(Page<BorrowRecord> page, Long readerId, String status);
    
    void borrowBook(Long readerId, Long bookId);
    
    void returnBook(Long id);
    
    Map<String, Object> getMyStats(Long readerId);
    
    List<Map<String, Object>> getTopBooks(Integer limit);
}