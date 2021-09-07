package cn.aixuxi.ossp.business.file.service.impl;

import cn.aixuxi.ossp.business.file.mapper.FileMapper;
import cn.aixuxi.ossp.business.file.model.FileInfo;
import cn.aixuxi.ossp.business.file.service.IFileService;
import cn.aixuxi.ossp.business.file.utils.FileUtil;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.oss.model.ObjectInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件处理抽象类，根据ossp.file-server.type实例化具体对象
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 17:17
 **/
@Slf4j
public abstract class AbstractIFileService extends ServiceImpl<FileMapper, FileInfo> implements IFileService {

    private static final String FILE_SPLIT = ".";

    @Override
    public FileInfo upload(MultipartFile file) throws Exception {
        FileInfo fileInfo = FileUtil.getFileInfo(file);
        if (!fileInfo.getName().contains(FILE_SPLIT)){
            throw new IllegalArgumentException("缺少后缀名");
        }
        ObjectInfo objectInfo = uploadFile(file);
        fileInfo.setPath(objectInfo.getObjectPath());
        fileInfo.setUrl(objectInfo.getObjecUrl());
        // 设置文件来源
        fileInfo.setSource(fileType());
        // 将文件信息保存到数据库
        baseMapper.insert(fileInfo);
        return fileInfo;
    }

    /**
     * 文件来源
     * @return String
     */
    protected abstract String fileType();

    /**
     * 上传文件
     * @param file 文件
     * @return ObjectInfo
     */
    protected abstract ObjectInfo uploadFile(MultipartFile file);

    /**
     * 删除文件资源
     * @param objectPath 文件路径
     */
    protected abstract void deleteFile(String objectPath);

    @Override
    public PageResult<FileInfo> findList(Map<String, Object> params) {
        Page<FileInfo> page = new Page<>(MapUtils.getInteger(params,"page"),MapUtils.getInteger(params,"limit"));
        List<FileInfo> list = baseMapper.findList(page,params);
        return PageResult.<FileInfo>builder().data(list).code(0).count(page.getTotal()).build();
    }

    @Override
    public void delete(String id) {
        FileInfo fileInfo = baseMapper.selectById(id);
        if (fileInfo != null){
            baseMapper.deleteById(fileInfo.getId());
            this.deleteFile(fileInfo.getPath());
        }
    }

}
