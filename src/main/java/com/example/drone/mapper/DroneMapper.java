/*MyBatis接口，定义数据库增删改查抽象方法（selectAll、insert、update 等） */
package com.example.drone.mapper;

import com.example.drone.entity.Drone;//导入无人机实体类
import org.apache.ibatis.annotations.Mapper;//MyBatis 核心注解，告诉Spring这是Mapper接口，自动生成数据库代理实现类

import java.util.List;//用于返回多条无人机数据

/**
 * 无人机数据访问层接口
 * 提供无人机实体的 CRUD 操作方法
 */
@Mapper
public interface DroneMapper {

    /**
     * 查询所有无人机列表
     * @return 无人机列表
     */
    List<Drone> findAll();//各种抽象方法

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
     * 插入新无人机记录
     * @param drone 无人机实体
     */
    void insert(Drone drone);

    /**
     * 更新无人机记录
     * @param drone 无人机实体
     */
    void update(Drone drone);

    /**
     * 根据ID删除无人机记录
     * @param id 无人机ID
     */
    void deleteById(Long id);
}