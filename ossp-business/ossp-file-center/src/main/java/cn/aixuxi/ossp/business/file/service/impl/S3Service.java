package cn.aixuxi.ossp.business.file.service.impl;

import cn.aixuxi.ossp.business.file.model.FileInfo;
import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.oss.model.ObjectInfo;
import cn.aixuxi.ossp.common.oss.properties.FileServerProperties;
import cn.aixuxi.ossp.common.oss.template.S3Template;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.OutputStream;

/**
 * S3服务
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 17:31
 **/
@Service
@ConditionalOnProperty(prefix = FileServerProperties.PREFIX, name = "type", havingValue = FileServerProperties.TYPE_S3)
public class S3Service extends AbstractIFileService {
    @Resource
    private S3Template template;

    @Override
    public void out(String id, OutputStream os) {
        FileInfo fileInfo = baseMapper.selectById(id);
        if (fileInfo != null){
            S3Object s3Object = parsePath(fileInfo.getPath());
            template.out(s3Object.bucketName,s3Object.objectName,os);
        }
    }

    private S3Object parsePath(String path) {
        S3Object s3Object = new S3Object();
        if (StrUtil.isNotEmpty(path)){
            int splitIndex = path.lastIndexOf(CommonConstant.PATH_SPLIT);
            if (splitIndex != -1){
                s3Object.bucketName = path.substring(0,splitIndex);
                s3Object.objectName = path.substring(splitIndex+1);
            }
        }
        return s3Object;
    }


    /**
     * 文件来源
     *
     * @return String
     */
    @Override
    protected String fileType() {
        return FileServerProperties.TYPE_S3;
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return ObjectInfo
     */
    @Override
    protected ObjectInfo uploadFile(MultipartFile file) {
        return template.upload(file);
    }

    /**
     * 删除文件资源
     *
     * @param objectPath 文件路径
     */
    @Override
    protected void deleteFile(String objectPath) {
        S3Object s3Object = parsePath(objectPath);
        template.delete(s3Object.bucketName,s3Object.objectName);
    }

    @Getter
    @Setter
    private class S3Object{
        private String bucketName;
        private String objectName;
    }
}
