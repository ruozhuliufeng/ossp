package cn.aixuxi.ossp.uaa.dto;

import cn.aixuxi.ossp.uaa.model.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * Client 数据传输DTO
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 11:27
 **/
@Getter
@Setter
public class ClientDTO extends Client {
    private static final long serialVersionUID = 1475637288060027265L;

    /**
     * 权限id集合
     */
    private List<Long> permissionIds;

    /**
     * 服务id集合
     */
    private Set<Long> serviceIds;

}
