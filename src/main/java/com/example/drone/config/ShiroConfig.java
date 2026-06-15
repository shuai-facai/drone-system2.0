/*权限安全配置 解决前端无法调用接口的权限拦截问题*/
package com.example.drone.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 安全配置类
 * 配置用户认证和授权策略
 */
@Configuration
public class ShiroConfig {

    /**
     * 创建 Realm 实例
     * 配置内存中的用户和角色信息
     */
    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        // 配置用户：admin(密码123,角色admin), user(密码user,角色user)
        realm.setUserDefinitions("admin=123,admin\nuser=user,user");
        // 配置角色权限：admin拥有所有权限
        realm.setRoleDefinitions("admin=*");
        return realm;
    }

    /**
     * 创建安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 创建 Shiro 过滤器工厂
     * 配置 URL 访问权限规则
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();//创建权限规则 Map
        
        // 静态资源允许匿名访问
        //首页、前端静态文件（js/css/ 图片）全部放开，游客可以直接加载页面，不会被登录拦截。
        //权限中用来存储「请求路径 - 权限规则」的有序 Map
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        
        // API 接口权限配置（登陆相关接口放行）
        filterChainDefinitionMap.put("/api/login", "anon");
        filterChainDefinitionMap.put("/api/logout", "anon");
        filterChainDefinitionMap.put("/api/checkAuth", "anon");
        
        // 其他所有请求允许匿名访问（前后端分离模式下前端处理认证）（全局全部路径放行）
        filterChainDefinitionMap.put("/**", "anon");
//把规则注入过滤器并返回
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}
