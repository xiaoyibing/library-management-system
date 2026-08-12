package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.BookShelfRelation;
import com.example.bookprogram1.service.BookShelfRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/shelf-relations")
@RequiredArgsConstructor
public class BookShelfRelationController {

    private final BookShelfRelationService bookShelfRelationService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long shelfId,
            @RequestParam(required = false) Long categoryId) {
        
        LambdaQueryWrapper<BookShelfRelation> wrapper = new LambdaQueryWrapper<>();
        if (bookId != null) {
            wrapper.eq(BookShelfRelation::getBookId, bookId);
        }
        if (shelfId != null) {
            wrapper.eq(BookShelfRelation::getShelfId, shelfId);
        }
        if (categoryId != null) {
            wrapper.eq(BookShelfRelation::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(BookShelfRelation::getCreateTime);
        
        Page<BookShelfRelation> pageResult = bookShelfRelationService.page(new Page<>(page, size), wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<BookShelfRelation> getById(@PathVariable Long id) {
        return Result.success(bookShelfRelationService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody BookShelfRelation relation) {
        bookShelfRelationService.save(relation);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody BookShelfRelation relation) {
        relation.setId(id);
        bookShelfRelationService.updateById(relation);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookShelfRelationService.removeById(id);
        return Result.success();
    }
}