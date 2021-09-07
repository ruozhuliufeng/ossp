package cn.aixuxi.ossp.business.file.service.impl;

import cn.aixuxi.ossp.common.oss.model.ObjectInfo;
import cn.aixuxi.ossp.common.oss.properties.FileServerProperties;
import cn.aixuxi.ossp.common.oss.template.FdfsTemplate;
import org.apache.commons.math3.analysis.function.Abs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.OutputStream;

/**
 * FastDfs实现
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 17:28
 **/
@Service
@ConditionalOnProperty(prefix = FileServerProperties.PREFIX,name = "type",havingValue = FileServerProperties.TYPE_FDFS)
public class FastdfsService extends AbstractIFileService {
    @Resource
    private FdfsTemplate fdfsTemplate;

    @Override
    public void out(String id, OutputStream os) {

    }

    /**
     * 文件来源
     *
     * @return String
     */
    @Override
    protected String fileType() {
        return FileServerProperties.TYPE_FDFS;
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return ObjectInfo
     */
    @Override
    protected ObjectInfo uploadFile(MultipartFile file) {
        return fdfsTemplate.upload(file);
    }

    /**
     * 删除文件资源
     *
     * @param objectPath 文件路径
     */
    @Override
    protected void deleteFile(String objectPath) {
        fdfsTemplate.delete(objectPath);
    }
}
