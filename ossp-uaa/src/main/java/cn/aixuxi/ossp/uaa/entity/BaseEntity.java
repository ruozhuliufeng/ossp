package cn.aixuxi.ossp.uaa.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/17 22:23
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 创建用户
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标识 0-未删除 1-已删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delete;
}
