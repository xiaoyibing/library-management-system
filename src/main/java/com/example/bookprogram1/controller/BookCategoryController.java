package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.BookCategory;
import com.example.bookprogram1.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        
        LambdaQueryWrapper<BookCategory> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(BookCategory::getName, name);
        }
        wrapper.orderByDesc(BookCategory::getCreateTime);
        
        Page<BookCategory> pageResult = bookCategoryService.page(new Page<>(page, size), wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<BookCategory> getById(@PathVariable Long id) {
        return Result.success(bookCategoryService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody BookCategory category) {
        bookCategoryService.save(category);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody BookCategory category) {
        category.setId(id);
        bookCategoryService.updateById(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookCategoryService.removeById(id);
        return Result.success();
    }
}