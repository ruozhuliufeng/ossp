package cn.aixuxi.ossp.search.client.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:10
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDTO implements Serializable {
    private static final long serialVersionUID = -2084416068307485742L;
    /**
     * 搜索关键字
     */
    private String queryStr;
    /**
     * 当前页数
     */
    private Integer page;
    /**
     * 每页显示数
     */
    private Integer limit;
    /**
     * 排序字段
     */
    private String sortCol;
    /**
     * 是否显示高亮
     */
    private Boolean isHighlighter;
    /**
     * ES的路由
     */
    private String routing;
}
