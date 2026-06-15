/*业务接口，定义无人机业务方法*/
package com.example.drone.service;

import com.example.drone.entity.Drone;

import java.util.List;

/**
 * 无人机服务接口
 * 提供无人机业务逻辑操作方法
 */
public interface DroneService {

    /**
     * 查询所有无人机列表
     * @return 无人机列表
     */
    List<Drone> findAll();

    /**
     * 根据ID查询无人机
     * @param id 无人机ID
     * @return 无人机实体
     */
    Drone findById(Long id);

    /**
     * 根据型号查询无人机列表
     * @param model 无人机型号
     * @return 无人机列表
     */
    List<Drone> findByModel(String model);

    /**
     * 根据无人机编号查询无人机列表
     * @param uavCode 无人机编号
     * @return 无人机列表
     */
    List<Drone> findByUavCode(String uavCode);

    /**
     * 保存新无人机
     * @param drone 无人机实体
     */
    void save(Drone drone);

    /**
     * 更新无人机信息
     * @param drone 无人机实体
     */
    void update(Drone drone);

    /**
     * 根据ID删除无人机
     * @param id 无人机ID
     */
    void deleteById(Long id);

    /**
     * 插入新无人机记录
     * @param drone 无人机实体
     */
    void insert(Drone drone);

    /**
     * 使用AI自动生成无人机数据
     * @param count 生成数量
     * @return 生成的无人机列表
     */
    List<Drone> generateByAI(int count);
}