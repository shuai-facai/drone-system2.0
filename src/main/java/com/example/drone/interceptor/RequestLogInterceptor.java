/*请求日志拦截器，拦截所有浏览器请求，记录访问地址、请求参数、访问时间*/
package com.example.drone.interceptor;

//导入日志工具、Spring 拦截器接口、请求 / 响应对象
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求日志拦截器
 * 记录所有HTTP请求的详细信息，便于日志追踪和问题排查
 */
public class RequestLogInterceptor implements HandlerInterceptor {//实现 HandlerInterceptor 接口 → 成为 SpringMVC 请求拦截器
    //创建日志对象
    private static final Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

    /**
     * 请求处理前记录日志
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器
     * @return 是否继续处理
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String url = request.getRequestURI();//获取访问路径
        String queryString = request.getQueryString();
        String clientIp = getClientIp(request);
        //功能：记录谁在什么时间、用什么方式、访问了什么接口
        logger.info("请求信息 - 方法: {}, URL: {}, 查询参数: {}, 客户端IP: {}", 
                method, url, queryString, clientIp);
        
        return true;
    }

    /**
     * 请求处理后（Controller 执行完、返回前端之前执行）记录响应状态
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器
     * @param modelAndView 视图模型
     * @throws Exception 异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url = request.getRequestURI();
        int statusCode = response.getStatus();
        //记录接口返回的状态码
        logger.info("响应信息 - URL: {}, 状态码: {}", url, statusCode);
    }

    /**
     * 请求完成后处理执行，记录异常信息，方便排查 bug
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器
     * @param ex 异常（如有）
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            //如果请求过程中报错了，在这里捕获并记录异常信息
            logger.error("请求完成时发生异常 - URL: {}, 异常: {}", request.getRequestURI(), ex.getMessage());
        }
    }

    /**
     * 获取客户端真实IP地址
     * 支持通过代理服务器获取真实IP
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        //兼容代理、Nginx、网关等多层转发
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}