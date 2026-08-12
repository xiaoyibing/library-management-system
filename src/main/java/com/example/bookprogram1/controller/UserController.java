package com.example.bookprogram1.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.entity.Reader;
import com.example.bookprogram1.entity.SysUser;
import com.example.bookprogram1.service.ReaderService;
import com.example.bookprogram1.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;
    private final ReaderService readerService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role) {
        
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (StringUtils.hasText(role)) {
            wrapper.eq(SysUser::getRole, role);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        
        Page<SysUser> pageResult = sysUserService.page(new Page<>(page, size), wrapper);
        
        // 移除密码字段
        pageResult.getRecords().forEach(user -> user.setPassword(null));
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }

    @PostMapping
    public Result<Void> add(@RequestBody SysUser user) {
        // 检查用户名是否存在
        if (sysUserService.getByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sysUserService.save(user);
        
        // 如果是普通用户，自动创建读者记录
        if ("USER".equals(user.getRole())) {
            Reader reader = new Reader();
            reader.setUserId(user.getId());
            reader.setCardNo("R" + String.format("%08d", user.getId()));
            reader.setName(user.getUsername());
            reader.setGender("男");
            readerService.save(reader);
        }
        
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        user.setPassword(null); // 不更新密码
        sysUserService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success();
    }

    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        sysUserService.resetPassword(id);
        return Result.success();
    }
}
