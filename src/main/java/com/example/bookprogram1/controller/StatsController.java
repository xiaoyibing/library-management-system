package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.Book;
import com.example.bookprogram1.entity.BorrowRecord;
import com.example.bookprogram1.entity.Reader;
import com.example.bookprogram1.service.BookService;
import com.example.bookprogram1.service.BorrowRecordService;
import com.example.bookprogram1.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final BookService bookService;
    private final ReaderService readerService;
    private final BorrowRecordService borrowRecordService;

    @GetMapping("/dashboard")  // 明确指定dashboard端点
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 图书总数
        long bookCount = bookService.count();
        stats.put("bookCount", bookCount);
        
        // 读者总数
        long readerCount = readerService.count();
        stats.put("readerCount", readerCount);
        
        // 借阅中数量
        long borrowCount = borrowRecordService.count(
                new LambdaQueryWrapper<BorrowRecord>().eq(BorrowRecord::getStatus, "BORROWED"));
        stats.put("borrowCount", borrowCount);
        
        // 今日借阅
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long todayBorrow = borrowRecordService.count(
                new LambdaQueryWrapper<BorrowRecord>().ge(BorrowRecord::getBorrowTime, todayStart));
        stats.put("todayBorrow", todayBorrow);
        
        // 今日归还
        long todayReturn = borrowRecordService.count(
                new LambdaQueryWrapper<BorrowRecord>()
                        .ge(BorrowRecord::getReturnTime, todayStart)
                        .isNotNull(BorrowRecord::getReturnTime));
        stats.put("todayReturn", todayReturn);
        
        return Result.success(stats);
    }

    @GetMapping
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 图书总数
        long bookCount = bookService.count();
        stats.put("bookCount", bookCount);
        
        // 读者总数
        long readerCount = readerService.count();
        stats.put("readerCount", readerCount);
        
        // 借阅中数量
        long borrowCount = borrowRecordService.count(
                new LambdaQueryWrapper<BorrowRecord>().eq(BorrowRecord::getStatus, "BORROWED"));
        stats.put("borrowCount", borrowCount);
        
        // 今日借阅
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long todayBorrow = borrowRecordService.count(
                new LambdaQueryWrapper<BorrowRecord>().ge(BorrowRecord::getBorrowTime, todayStart));
        stats.put("todayBorrow", todayBorrow);
        
        // 今日归还
        long todayReturn = borrowRecordService.count(
                new LambdaQueryWrapper<BorrowRecord>()
                        .ge(BorrowRecord::getReturnTime, todayStart)
                        .isNotNull(BorrowRecord::getReturnTime));
        stats.put("todayReturn", todayReturn);
        
        return Result.success(stats);
    }

    @GetMapping("/borrow-trend")
    public Result<Map<String, Object>> getBorrowTrend() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取最近7天的借阅趋势数据
        LocalDateTime start = LocalDate.now().minusDays(6).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay().plusDays(1);
        
        // 获取借阅记录
        List<BorrowRecord> records = borrowRecordService.list(
                new LambdaQueryWrapper<BorrowRecord>()
                        .between(BorrowRecord::getBorrowTime, start, end)
                        .orderByAsc(BorrowRecord::getBorrowTime)
        );
        
        // 按日期统计借阅数量
        Map<String, Long> dailyCount = new HashMap<>();
        for (BorrowRecord record : records) {
            String date = record.getBorrowTime().toLocalDate().toString();
            dailyCount.put(date, dailyCount.getOrDefault(date, 0L) + 1);
        }
        
        // 生成7天的日期范围
        result.put("trend", generateTrendData(start, end, dailyCount));
        
        return Result.success(result);
    }

    @GetMapping("/top-books")
    public Result<Map<String, Object>> getTopBooks(@RequestParam(defaultValue = "5") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        
        // 统计每本书的借阅次数
        List<Map<String, Object>> topBooks = borrowRecordService.getTopBooks(limit);
        
        // 返回图书信息和借阅次数
        result.put("books", topBooks);
        
        return Result.success(result);
    }

    // 辅助方法：生成趋势数据
    private List<Map<String, Object>> generateTrendData(LocalDateTime start, LocalDateTime end, Map<String, Long> dailyCount) {
        LocalDate current = start.toLocalDate();
        LocalDate endDate = end.toLocalDate().minusDays(1);
        
        java.util.List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        while (!current.isAfter(endDate)) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", current.toString());
            item.put("count", dailyCount.getOrDefault(current.toString(), 0L));
            trendData.add(item);
            current = current.plusDays(1);
        }
        
        return trendData;
    }
}