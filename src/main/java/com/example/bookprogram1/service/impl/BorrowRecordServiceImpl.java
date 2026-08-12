package com.example.bookprogram1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprogram1.entity.Book;
import com.example.bookprogram1.entity.BorrowRecord;
import com.example.bookprogram1.mapper.BorrowRecordMapper;
import com.example.bookprogram1.service.BookService;
import com.example.bookprogram1.service.BorrowRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    private final BookService bookService;

    @Override
    public IPage<BorrowRecord> getPageWithDetail(Page<BorrowRecord> page, String status) {
        return baseMapper.selectPageWithDetail(page, status);
    }

    @Override
    public IPage<BorrowRecord> getMyPageWithDetail(Page<BorrowRecord> page, Long readerId, String status) {
        return baseMapper.selectMyPageWithDetail(page, readerId, status);
    }

    @Override
    @Transactional
    public void borrowBook(Long readerId, Long bookId) {
        Book book = bookService.getById(bookId);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }
        if (book.getAvailable() <= 0) {
            throw new RuntimeException("图书库存不足");
        }

        // 更新图书可借数量
        book.setAvailable(book.getAvailable() - 1);
        bookService.updateById(book);

        // 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setReaderId(readerId);
        record.setBookId(bookId);
        record.setBorrowTime(LocalDateTime.now());
        record.setDueTime(LocalDateTime.now().plusDays(30));
        record.setStatus("BORROWED");
        save(record);
    }

    @Override
    @Transactional
    public void returnBook(Long id) {
        BorrowRecord record = getById(id);
        if (record == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        if (!"BORROWED".equals(record.getStatus())) {
            throw new RuntimeException("该图书已归还");
        }

        // 更新借阅记录
        record.setReturnTime(LocalDateTime.now());
        record.setStatus("RETURNED");
        updateById(record);

        // 更新图书可借数量
        Book book = bookService.getById(record.getBookId());
        book.setAvailable(book.getAvailable() + 1);
        bookService.updateById(book);
    }

    @Override
    public Map<String, Object> getMyStats(Long readerId) {
        Map<String, Object> stats = new HashMap<>();
        
        long borrowing = count(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getReaderId, readerId)
                .eq(BorrowRecord::getStatus, "BORROWED"));
        
        long returned = count(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getReaderId, readerId)
                .eq(BorrowRecord::getStatus, "RETURNED"));
        
        long overdue = count(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getReaderId, readerId)
                .eq(BorrowRecord::getStatus, "OVERDUE"));
        
        stats.put("borrowing", borrowing);
        stats.put("returned", returned);
        stats.put("overdue", overdue);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getTopBooks(Integer limit) {
        return baseMapper.selectTopBooks(limit);
    }
}