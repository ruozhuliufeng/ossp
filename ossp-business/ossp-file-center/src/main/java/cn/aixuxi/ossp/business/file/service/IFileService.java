package cn.aixuxi.ossp.business.file.service;

import cn.aixuxi.ossp.business.file.model.FileInfo;
import cn.aixuxi.ossp.common.model.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.Map;

/**
 * 文件Service
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 17:15
 **/
public interface IFileService extends IService<FileInfo> {
    FileInfo upload(MultipartFile file) throws Exception;

    PageResult<FileInfo> findList(Map<String,Object> params);

    void delete(String id);

    void out(String id, OutputStream os);
}
