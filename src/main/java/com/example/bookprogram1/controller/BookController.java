package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.Book;
import com.example.bookprogram1.service.BookCategoryService;
import com.example.bookprogram1.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookCategoryService bookCategoryService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long categoryId) {
        
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Book::getName, name);
        }
        if (StringUtils.hasText(author)) {
            wrapper.like(Book::getAuthor, author);
        }
        if (categoryId != null) {
            wrapper.eq(Book::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Book::getCreateTime);
        
        Page<Book> pageResult = bookService.page(new Page<>(page, size), wrapper);
        
        // 为图书添加分类名称
        List<Book> booksWithCategoryName = pageResult.getRecords().stream().map(book -> {
            if (book.getCategoryId() != null) {
                var category = bookCategoryService.getById(book.getCategoryId());
                if (category != null) {
                    book.setCategoryName(category.getName());
                }
            }
            return book;
        }).collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", booksWithCategoryName);
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Book> getById(@PathVariable Long id) {
        Book book = bookService.getById(id);
        if (book != null && book.getCategoryId() != null) {
            var category = bookCategoryService.getById(book.getCategoryId());
            if (category != null) {
                book.setCategoryName(category.getName());
            }
        }
        return Result.success(book);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Book book) {
        book.setAvailable(book.getStock());
        bookService.save(book);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateById(book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookService.removeById(id);
        return Result.success();
    }
}