package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.Reader;
import com.example.bookprogram1.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {
        
        LambdaQueryWrapper<Reader> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Reader::getName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(Reader::getPhone, phone);
        }
        wrapper.orderByDesc(Reader::getCreateTime);
        
        Page<Reader> pageResult = readerService.page(new Page<>(page, size), wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Reader> getById(@PathVariable Long id) {
        return Result.success(readerService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Reader reader) {
        readerService.save(reader);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Reader reader) {
        reader.setId(id);
        readerService.updateById(reader);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        readerService.removeById(id);
        return Result.success();
    }
}
