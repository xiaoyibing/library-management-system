package com.example.bookprogram1.controller;

import com.example.bookprogram1.common.Result;
import com.example.bookprogram1.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService sysUserService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        try {
            Map<String, Object> result = sysUserService.login(username, password);
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        // 从SecurityContext获取用户信息
        return Result.success();
    }
}
