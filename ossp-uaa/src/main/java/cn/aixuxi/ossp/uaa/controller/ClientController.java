package cn.aixuxi.ossp.uaa.controller;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.uaa.dto.ClientDTO;
import cn.aixuxi.ossp.uaa.model.Client;
import cn.aixuxi.ossp.uaa.service.IClientService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 应用相关接口
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 11:27
 **/
@RestController
@Api(tags = "应用")
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @GetMapping("list")
    @ApiOperation(value = "应用列表")
    public PageResult<Client> list(@RequestParam Map<String,Object> params){
        return clientService.listClient(params,true);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取应用")
    public Client get(@PathVariable("id") Long id){
        return clientService.getById(id);
    }

    @GetMapping("/all")
    @ApiOperation(value = "所有应用")
    public Result<List<Client>> allClient(){
        PageResult<Client> page = clientService.listClient(Maps.newHashMap(),false);
        return Result.succeed(page.getData());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除应用")
    public void delete(@PathVariable("id") Long id){
        clientService.delClient(id);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或修改应用")
    public Result saveOrUpdate(@RequestBody ClientDTO clientDTO) throws Exception{
        return clientService.saveClient(clientDTO);
    }
}
