/*pringMVC 全局配置：跨域、静态资源映射、拦截器注册等 */
package com.example.drone.config;

import com.example.drone.interceptor.RequestLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 配置跨域支持、拦截器等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {//实现接口

    /**
     * 添加请求日志拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLogInterceptor())//把自定义的RequestLogInterceptor请求日志拦截器注册到全局
                .addPathPatterns("/**");//拦截所有请求路径，也就是用户访问任意接口 / 页面都会先走这个拦截器
    }

    /**
     * 配置跨域访问支持
     * 允许前端访问后端 API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")//只对所有/api/开头的接口启用跨域配置
                .allowedOrigins("http://localhost:5173", "http://localhost:8080", "http://localhost:8888")//允许这三个前端域名跨域调用后端接口
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")//允许前端使用这五种请求方式
                .allowedHeaders("*")//允许前端携带任意请求头
                .allowCredentials(true)//允许跨域携带 Cookie、会话凭证（登录态）
                .maxAge(3600);//浏览器缓存预检请求 1 小时，减少重复 OPTIONS 请求
    }
}
