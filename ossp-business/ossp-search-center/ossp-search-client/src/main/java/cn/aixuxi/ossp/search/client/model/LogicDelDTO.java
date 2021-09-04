package cn.aixuxi.ossp.search.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 逻辑删除条件对象
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:13
 **/
@Setter
@Getter
@AllArgsConstructor
public class LogicDelDTO {
    /**
     * 逻辑删除字段名
     */
    private String logicDelFiels;
    /**
     * 逻辑删除字段未删除的值
     */
    private String logicnotDelValue;
}
