/*接口实现类，业务代码写在这里，调用 mapper 操作数据库，处理业务规则（比如数据校验、统计） */
package com.example.drone.service.impl;

import com.example.drone.entity.Drone;
import com.example.drone.mapper.DroneMapper;
import com.example.drone.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 无人机服务实现类
 * 实现无人机业务逻辑，包括CRUD操作和AI生成功能
 */
@Service
public class DroneServiceImpl implements DroneService {//实现服务接口，必须重写所有方法

    /** 无人机数据访问层 */
    private final DroneMapper droneMapper;
    /** 随机数生成器，用于AI生成 */
    private final Random random = new Random();

    /** 制造商列表 */
    private static final String[] MANUFACTURERS = {"DJI", "Parrot", "Autel Robotics", "Yuneec", "Skydio", "AI生成品牌"};
    /** 型号前缀列表 */
    private static final String[] MODEL_PREFIXES = {"探索者", "翱翔者", "鹰眼", "飞翼", "云端", "巡航者", "迅捷", "领航者"};
    /** 型号后缀列表 */
    private static final String[] MODEL_SUFFIXES = {"Pro", "Ultra", "X", "Plus", "Max", "Advanced", "S", "Elite"};
    /** 无人机类别列表 */
    private static final String[] CATEGORIES = {"消费级", "工业级", "农业级", "测绘级", "安防级"};

    /**
     * 构造函数，注入DroneMapper
     * @param droneMapper 无人机数据访问层
     */
    @Autowired
    public DroneServiceImpl(DroneMapper droneMapper) {//所有数据库操作都通过 droneMapper 调用，不直接操作数据库表
        this.droneMapper = droneMapper;
    }

    /**
     * 查询所有无人机
     * @return 无人机列表
     */
    @Override
    public List<Drone> findAll() {
        return droneMapper.findAll();
    }

    /**
     * 根据ID查询无人机
     * @param id 无人机ID
     * @return 无人机实体
     */
    @Override
    public Drone findById(Long id) {
        return droneMapper.findById(id);
    }

    /**
     * 根据型号查询无人机列表
     * @param model 无人机型号
     * @return 无人机列表
     */
    @Override
    public List<Drone> findByModel(String model) {
        return droneMapper.findByModel(model);
    }

    /**
     * 根据无人机编号查询无人机列表
     * @param uavCode 无人机编号
     * @return 无人机列表
     */
    @Override
    public List<Drone> findByUavCode(String uavCode) {
        return droneMapper.findByUavCode(uavCode);
    }

    /**
     * 保存无人机
     * @param drone 无人机实体
     */
    @Override
    public void save(Drone drone) {
        if (drone.getStatus() == null) {//没传状态默认正常（1）
            drone.setStatus(1);
        }
        if (drone.getAiGenerated() == null) {//没传AI标记默认手动录入（0）
            drone.setAiGenerated(0);
        }
        droneMapper.insert(drone);
    }

    /**
     * 更新无人机信息
     * @param drone 无人机实体
     */
    @Override
    public void update(Drone drone) {
        droneMapper.update(drone);
    }

    /**
     * 根据ID删除无人机
     * @param id 无人机ID
     */
    @Override
    public void deleteById(Long id) {
        droneMapper.deleteById(id);
    }

    /**
     * 插入无人机
     * @param drone 无人机实体
     */
    @Override
    public void insert(Drone drone) {
        if (drone.getStatus() == null) {
            drone.setStatus(1);
        }
        if (drone.getAiGenerated() == null) {
            drone.setAiGenerated(0);
        }
        droneMapper.insert(drone);
    }

    /**
     * 使用AI自动生成无人机数据
     * @param count 生成数量
     * @return 生成的无人机列表
     */
    @Override
    public List<Drone> generateByAI(int count) {//方法入口，接收生成数量，循环生成N台无人机。
        List<Drone> generatedDrones = new ArrayList<>();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        for (int i = 0; i < count; i++) {
            Drone drone = generateSingleDrone(timestamp, i);
            droneMapper.insert(drone);
            generatedDrones.add(drone);
        }

        return generatedDrones;
    }

    /**
     * 生成单个无人机实例
     * @param timestamp 时间戳
     * @param index 索引
     * @return 生成的无人机实例
     */
    private Drone generateSingleDrone(String timestamp, int index) {
        Drone drone = new Drone();
        
        String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
        
        drone.setUavCode("UAV-AI-" + timestamp + "-" + String.format("%03d", index + 1));
        drone.setModel(category + "-" + MODEL_PREFIXES[random.nextInt(MODEL_PREFIXES.length)] + "-" + MODEL_SUFFIXES[random.nextInt(MODEL_SUFFIXES.length)]);
        drone.setManufacturer(MANUFACTURERS[random.nextInt(MANUFACTURERS.length)]);
        
        setRandomSpecs(drone, category);//根据类别随机生成性能参数
        
        drone.setStatus(1);
        drone.setRemark("由AI规则引擎自动生成，类别：" + category);
        drone.setAiGenerated(1);

        return drone;
    }

    /**
     * 根据类别设置随机规格参数
     * @param drone 无人机实例
     * @param category 无人机类别
     */
    private void setRandomSpecs(Drone drone, String category) {
        switch (category) {
            case "消费级":
                drone.setMaxPayload(Math.round(random.nextDouble() * 1.5 + 0.2) * 10.0 / 10.0);
                drone.setMaxAltitude(random.nextInt(3000) + 1000);
                drone.setMaxFlightTime(random.nextInt(30) + 15);
                drone.setMaxSpeed(Math.round(random.nextDouble() * 8 + 10) * 10.0 / 10.0);
                drone.setWingspan(Math.round(random.nextDouble() * 30 + 20) * 10.0 / 10.0);
                drone.setWeight(Math.round(random.nextDouble() * 1.0 + 0.1) * 10.0 / 10.0);
                break;
            case "工业级":
                drone.setMaxPayload(Math.round(random.nextDouble() * 5 + 2) * 10.0 / 10.0);
                drone.setMaxAltitude(random.nextInt(3000) + 4000);
                drone.setMaxFlightTime(random.nextInt(40) + 30);
                drone.setMaxSpeed(Math.round(random.nextDouble() * 10 + 15) * 10.0 / 10.0);
                drone.setWingspan(Math.round(random.nextDouble() * 500 + 200) * 10.0 / 10.0);
                drone.setWeight(Math.round(random.nextDouble() * 8 + 4) * 10.0 / 10.0);
                break;
            case "农业级":
                drone.setMaxPayload(Math.round(random.nextDouble() * 10 + 5) * 10.0 / 10.0);
                drone.setMaxAltitude(random.nextInt(2000) + 1000);
                drone.setMaxFlightTime(random.nextInt(50) + 25);
                drone.setMaxSpeed(Math.round(random.nextDouble() * 8 + 12) * 10.0 / 10.0);
                drone.setWingspan(Math.round(random.nextDouble() * 400 + 300) * 10.0 / 10.0);
                drone.setWeight(Math.round(random.nextDouble() * 15 + 8) * 10.0 / 10.0);
                break;
            case "测绘级":
                drone.setMaxPayload(Math.round(random.nextDouble() * 3 + 1) * 10.0 / 10.0);
                drone.setMaxAltitude(random.nextInt(4000) + 3000);
                drone.setMaxFlightTime(random.nextInt(60) + 40);
                drone.setMaxSpeed(Math.round(random.nextDouble() * 12 + 18) * 10.0 / 10.0);
                drone.setWingspan(Math.round(random.nextDouble() * 300 + 150) * 10.0 / 10.0);
                drone.setWeight(Math.round(random.nextDouble() * 5 + 2) * 10.0 / 10.0);
                break;
            case "安防级":
                drone.setMaxPayload(Math.round(random.nextDouble() * 4 + 1) * 10.0 / 10.0);
                drone.setMaxAltitude(random.nextInt(3000) + 2000);
                drone.setMaxFlightTime(random.nextInt(45) + 25);
                drone.setMaxSpeed(Math.round(random.nextDouble() * 15 + 20) * 10.0 / 10.0);
                drone.setWingspan(Math.round(random.nextDouble() * 200 + 100) * 10.0 / 10.0);
                drone.setWeight(Math.round(random.nextDouble() * 6 + 3) * 10.0 / 10.0);
                break;
            default:
                drone.setMaxPayload(Math.round(random.nextDouble() * 2 + 0.5) * 10.0 / 10.0);
                drone.setMaxAltitude(random.nextInt(2000) + 1000);
                drone.setMaxFlightTime(random.nextInt(30) + 20);
                drone.setMaxSpeed(Math.round(random.nextDouble() * 10 + 10) * 10.0 / 10.0);
                drone.setWingspan(Math.round(random.nextDouble() * 50 + 30) * 10.0 / 10.0);
                drone.setWeight(Math.round(random.nextDouble() * 2 + 0.5) * 10.0 / 10.0);
        }
    }
}
