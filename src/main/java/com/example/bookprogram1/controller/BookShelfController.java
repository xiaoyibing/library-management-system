package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.BookShelf;
import com.example.bookprogram1.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/shelves")
@RequiredArgsConstructor
public class BookShelfController {

    private final BookShelfService bookShelfService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        
        LambdaQueryWrapper<BookShelf> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(BookShelf::getName, name);
        }
        wrapper.orderByDesc(BookShelf::getCreateTime);
        
        Page<BookShelf> pageResult = bookShelfService.page(new Page<>(page, size), wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<BookShelf> getById(@PathVariable Long id) {
        return Result.success(bookShelfService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody BookShelf shelf) {
        bookShelfService.save(shelf);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody BookShelf shelf) {
        shelf.setId(id);
        bookShelfService.updateById(shelf);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookShelfService.removeById(id);
        return Result.success();
    }
}