package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.BorrowRecord;
import com.example.bookprogram1.entity.Reader;
import com.example.bookprogram1.service.BorrowRecordService;
import com.example.bookprogram1.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowRecordService borrowRecordService;
    private final ReaderService readerService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        
        IPage<BorrowRecord> pageResult = borrowRecordService.getPageWithDetail(
                new Page<>(page, size), status);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @GetMapping("/my")
    public Result<Map<String, Object>> myList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        
        Long userId = getCurrentUserId();
        Reader reader = readerService.getByUserId(userId);
        if (reader == null) {
            return Result.error("读者信息不存在");
        }
        
        IPage<BorrowRecord> pageResult = borrowRecordService.getMyPageWithDetail(
                new Page<>(page, size), reader.getId(), status);
        
        Map<String, Object> stats = borrowRecordService.getMyStats(reader.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.putAll(stats);
        
        return Result.success(result);
    }

    @PostMapping
    public Result<Void> borrow(@RequestBody Map<String, Object> params) {
        Long bookId = Long.valueOf(params.get("bookId").toString());
        
        Long userId = getCurrentUserId();
        Reader reader = readerService.getByUserId(userId);
        if (reader == null) {
            return Result.error("读者信息不存在");
        }
        
        try {
            borrowRecordService.borrowBook(reader.getId(), bookId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/return")
    public Result<Void> returnBook(@PathVariable Long id) {
        try {
            borrowRecordService.returnBook(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
