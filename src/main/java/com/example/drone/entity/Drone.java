/*数据库表映射实体，属性和无人机数据表字段一一对应，MyBatis用来封装数据库查询数据 */
package com.example.drone.entity;
//用于新增/修改接口时自动校验前端传参合法性
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 无人机实体类
 * 表示无人机的基本信息
 */
public class Drone {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 注册编号
     */
    @NotBlank(message = "注册编号不能为空")
    @Size(max = 64, message = "注册编号长度不能超过64个字符")
    private String uavCode;

    /**
     * 型号
     */
    @NotBlank(message = "型号不能为空")
    @Size(max = 100, message = "型号长度不能超过100个字符")
    private String model;

    /**
     * 制造商
     */
    @Size(max = 100, message = "制造商长度不能超过100个字符")
    private String manufacturer;

    /**
     * 最大载重（kg）
     */
    private Double maxPayload;

    /**
     * 最大飞行高度（m）
     */
    private Integer maxAltitude;

    /**
     * 最大飞行时间（分钟）
     */
    private Integer maxFlightTime;

    /**
     * 最大速度（km/h）
     */
    private Double maxSpeed;

    /**
     * 翼展（m）
     */
    private Double wingspan;

    /**
     * 重量（kg）
     */
    private Double weight;

    /**
     * 状态：1-正常，0-禁用
     */
    private Integer status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * AI生成标记：1-AI生成，0-手动录入
     */
    private Integer aiGenerated;

    /**
     * 默认构造函数
     */
    public Drone() {//无参构造
    }
//字段是 private 封装的，外部不能直接访问，给所有属性提供getter，setter方法，MyBatis 要用它来封装数据库数据
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUavCode() {
        return uavCode;
    }

    public void setUavCode(String uavCode) {
        this.uavCode = uavCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getMaxPayload() {
        return maxPayload;
    }

    public void setMaxPayload(Double maxPayload) {
        this.maxPayload = maxPayload;
    }

    public Integer getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(Integer maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public Integer getMaxFlightTime() {
        return maxFlightTime;
    }

    public void setMaxFlightTime(Integer maxFlightTime) {
        this.maxFlightTime = maxFlightTime;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getWingspan() {
        return wingspan;
    }

    public void setWingspan(Double wingspan) {
        this.wingspan = wingspan;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAiGenerated() {
        return aiGenerated;
    }

    public void setAiGenerated(Integer aiGenerated) {
        this.aiGenerated = aiGenerated;
    }
}