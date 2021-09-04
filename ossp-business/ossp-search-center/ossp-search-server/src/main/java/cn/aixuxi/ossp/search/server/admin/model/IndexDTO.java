package cn.aixuxi.ossp.search.server.admin.model;

import lombok.Data;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:02
 **/
@Data
public class IndexDTO {
    /**
     * 索引名
     */
    private String indexName;

    /**
     * 分片数 number_of_shards
     */
    private Integer numberOfShards;

    /**
     * 副本数 number_of_replicas
     */
    private Integer numberOfReplicas;

    /**
     * 类型
     */
    private String type;
    /**
     * mappingsnr
     */
    private String mappingsSource;
}
