/*无人机数据 API 接口，对应地址/api/drones，
前端页面调用这个接口获取 / 新增 / 修改无人机数据 */
package com.example.drone.controller;

import com.example.drone.entity.Drone;//导入无人机实体类
import com.example.drone.service.DroneService;//导入无人机服务接口
import org.springframework.http.ResponseEntity;//导入响应实体类
import org.springframework.web.bind.annotation.*;//导入Spring Boot的注解，用于定义控制器和处理请求

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无人机控制器
 * 提供无人机的增删改查和AI生成功能
 */
@RestController
@RequestMapping("/api/drones")//映射路径/api/drones
public class DroneController {

    /** 无人机服务 */
    private final DroneService droneService;

    /**
     * 构造函数，注入DroneService
     * @param droneService 无人机服务
     */
    
    public DroneController(DroneService droneService) {//构造方法
        this.droneService = droneService;//把传入的service对象赋值给当前类的成员变量 droneService
        //之后 Controller 里所有接口方法，都可以通过 this.droneService 调用无人机业务逻辑
    }

    /**
     * 获取无人机列表
     * 支持按型号或编号搜索
     * @param keyword 搜索关键词
     * @param type 搜索类型（model或uavCode）
     * @return 无人机列表
     */
    @GetMapping//获取所有无人机

   //无人机列表查询接口，提供全局查询、按无人机编号搜索、按型号搜索能力
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "model") String type) {
        Map<String, Object> response = new HashMap<>();//初始化响应
        try {
            List<Drone> drones;
            if (keyword != null && !keyword.trim().isEmpty()) {
                // // 有关键词，按type区分搜索方式
                if ("uavCode".equals(type)) {
                    drones = droneService.findByUavCode(keyword);
                } else {
                    drones = droneService.findByModel(keyword);
                }
            } 
            //// 无关键词，查询全部无人机
            else {
                drones = droneService.findAll();
            }
            //正常查询成功
            response.put("success", true);
            response.put("data", drones);
        } catch (Exception e) {//异常捕获分支
            response.put("success", false);
            response.put("message", "查询失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);//返回 HTTP 200 状态码，把组装好的 Map 自动转为 JSON 返回前端
    }

    /**
     * 根据ID获取无人机详情
     * @param id 无人机ID
     * @return 无人机详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Drone drone = droneService.findById(id);//根据主键id查询单台无人机
            if (drone != null) {
                response.put("success", true);
                response.put("data", drone);
            } else {
                response.put("success", false);
                response.put("message", "无人机不存在");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "查询失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 创建新无人机
     * @param drone 无人机信息（入参是无人机实体）
     * @return 创建结果
     */
    @PostMapping//限定接口只接受POST请求
    public ResponseEntity<Map<String, Object>> create(@RequestBody Drone drone) {
        Map<String, Object> response = new HashMap<>();
        try {
            droneService.insert(drone);
            response.put("success", true);
            response.put("message", "添加成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 更新无人机信息
     * @param id 无人机ID
     * @param drone 无人机信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
        @PathVariable Long id, //从 URL 路径中取出要修改的无人机 id
        @RequestBody Drone drone) //接收前端传的 JSON 请求体，封装为无人机实体
        {
        Map<String, Object> response = new HashMap<>();
        try {
            drone.setId(id);//把路径上的ID赋值给前端传过来的实体
            droneService.update(drone);
            response.put("success", true);
            response.put("message", "更新成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 删除无人机
     * @param id 无人机ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            droneService.deleteById(id);
            response.put("success", true);
            response.put("message", "删除成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 使用AI生成无人机数据
     * @param request 包含生成数量的请求
     * @return 生成结果
     */
    @PostMapping("/ai/generate")
    public ResponseEntity<Map<String, Object>> generateByAI(@RequestBody Map<String, Integer> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int count = request.getOrDefault("count", 1);
            List<Drone> drones = droneService.generateByAI(count);
            response.put("success", true);
            response.put("data", drones);
            response.put("message", "生成成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "生成失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
