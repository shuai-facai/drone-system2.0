/*登录接口，处理账号密码登录（admin/123、user/user），对接 Shiro 登录认证逻辑 */
package com.example.drone.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证控制器
 * 提供用户登录、退出和认证状态检查功能
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    /**
     * 用户登录接口
     * @param loginData 包含用户名和密码的登录数据
     * @return 登录结果
     */
    @PostMapping("/login")  
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        //统一包装 HTTP 响应，返回自定义 JSON 格式结果       接收前端 JSON 请求体，用 Map 接收参数
        Map<String, Object> response = new HashMap<>();//新建 Map 作为统一返回体，统一返回 JSON 结构，success，message
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            response.put("success", true);
            response.put("message", "登录成功");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 用户退出接口
     * @return 退出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            response.put("success", true);
            response.put("message", "退出成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "退出失败");
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 检查用户认证状态接口
     * @return 当前用户的认证状态
     */
    @GetMapping("/checkAuth")//GET 请求，判断当前用户是否已经登录
    public ResponseEntity<Map<String, Object>> checkAuth() {
        Map<String, Object> response = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        //Subject代表当前访问用户，封装了登录状态、身份、权限等信息
        if (subject.isAuthenticated()) {//Shiro 提供的方法，判断用户是否已经登录
            response.put("success", true);
            response.put("username", subject.getPrincipal());
        } else {
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }
}
