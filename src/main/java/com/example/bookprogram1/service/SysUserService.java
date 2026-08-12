package com.example.bookprogram1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookprogram1.entity.SysUser;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    
    Map<String, Object> login(String username, String password);
    
    SysUser getByUsername(String username);
    
    void resetPassword(Long id);
}
