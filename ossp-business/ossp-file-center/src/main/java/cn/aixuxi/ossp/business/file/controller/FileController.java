package cn.aixuxi.ossp.business.file.controller;

import cn.aixuxi.ossp.business.file.model.FileInfo;
import cn.aixuxi.ossp.business.file.service.IFileService;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 文件上传
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-08 8:37
 **/
@RestController
public class FileController {
    @Resource
    private IFileService fileService;

    /**
     * 文件上传，根据fileType选择上传方式
     * @param file 文件
     * @return FileInfo
     * @throws Exception 上传异常
     */
    @PostMapping("/files-anon")
    public FileInfo upload(@RequestParam("file") MultipartFile file) throws Exception{
        return fileService.upload(file);
    }

    /**
     * 文件删除
     * @param id 文件id
     * @return Result
     */
    @DeleteMapping("/files/{id}")
    public Result delete(@PathVariable("id") String id){
        try {
            fileService.delete(id);
            return Result.succeed("操作成功");
        }catch (Exception e){
            return Result.failed("操作失败");
        }
    }

    /**
     * 文件查询
     * @param params 查询参数
     * @return PageResult
     */
    public PageResult<FileInfo> findFiles(@RequestParam Map<String,Object> params){
        return fileService.findList(params);
    }
}
