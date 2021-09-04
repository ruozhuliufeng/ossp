package cn.aixuxi.ossp.common.oss.template;

import cn.aixuxi.ossp.common.oss.model.ObjectInfo;
import cn.aixuxi.ossp.common.oss.properties.FileServerProperties;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * FastDFS配置
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 16:31
 **/
@ConditionalOnClass(FastFileStorageClient.class)
@ConditionalOnProperty(prefix = FileServerProperties.PREFIX, name = "type", havingValue = FileServerProperties.TYPE_FDFS)
public class FdfsTemplate {
    @Resource
    private FileServerProperties fileServerProperties;
    @Resource
    private FastFileStorageClient storageClient;

    @SneakyThrows
    public ObjectInfo upload(String objectName, InputStream inputStream) {
        return upload(objectName, inputStream, inputStream.available());
    }

    @SneakyThrows
    public ObjectInfo upload(MultipartFile file) {
        return upload(file.getOriginalFilename(), file.getInputStream());
    }

    /**
     * 上传对象
     *
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param size        大小
     * @return ObjectInfo
     */
    private ObjectInfo upload(String objectName, InputStream inputStream, long size) {
        StorePath storePath = storageClient.uploadFile(inputStream, size, FilenameUtils.getExtension(objectName), null);
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setObjectPath(storePath.getFullPath());
        objectInfo.setObjecUrl("http://" + fileServerProperties.getFdfs().getWebUrl());
        ;
        return objectInfo;
    }

    /**
     * 删除对象
     *
     * @param objectPath 对象路径
     */
    public void delete(String objectPath) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        }
    }

    /**
     * 下载对象
     *
     * @param objectPath 对象路径
     * @param callback   回调
     */
    public <T> T download(String objectPath, DownloadCallback<T> callback) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
        }
        return null;
    }
}
