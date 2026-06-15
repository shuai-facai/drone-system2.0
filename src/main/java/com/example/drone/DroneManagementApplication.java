/*项目SpringBoot 启动类，运行这个类就能在8080
端口启动整个系统，触发 DatabaseInitConfig 自动建表 */
package com.example.drone;

import org.mybatis.spring.annotation.MapperScan;//扫描Mapper接口，自动创建实现类
import org.springframework.boot.SpringApplication;// 应用程序主入口，用于启动Spring Boot应用
import org.springframework.boot.autoconfigure.SpringBootApplication;// 自动配置Spring Boot应用，扫描所有组件并加载到上下文


/**
 * 无人机管理系统启动类
 * Spring Boot应用的入口点
 */
@SpringBootApplication
@MapperScan("com.example.drone.mapper")
public class DroneManagementApplication {

    /**
     * 应用程序主入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(DroneManagementApplication.class, args);
        //Java 程序固定入口，run()方法启动整个 SpringBoot应用，启动后，前端就能访问接口了
    }
}