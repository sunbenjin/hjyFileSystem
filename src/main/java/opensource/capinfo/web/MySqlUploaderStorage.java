package opensource.capinfo.web;

import opensource.capinfo.dao.SysResourcesFilesRepository;
import opensource.capinfo.entity.FileInputParams;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.service.SysResourcesFilesService;
import opensource.capinfo.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName MySqlUploaderStorage
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/27 0027 16:18
 */
public class MySqlUploaderStorage implements UploaderStorage{

    private SysResourcesFilesService sysResourcesFilesService = (SysResourcesFilesService)SpringUtil.getBean("sysResourcesFilesService");


    /**
     *
     * @param entity
     * @param fileInputParams
     */
    @Override
    public void save(SysResourcesFilesEntity entity, FileInputParams fileInputParams) {
        entity.setId(fileInputParams.getKey());
                entity.setBusiId(fileInputParams.getBusiId());
        entity.setFilesDynCode(fileInputParams.getFilesDynCode());
        entity.setFileUniqueCode(fileInputParams.getFileUniqueCode());
        entity.setSysId(fileInputParams.getTableName());
        entity.setFlag(fileInputParams.getFlag());
        ;//就是主键ID
        sysResourcesFilesService.save(entity);

    }

    @Override
    public void updateFileData(SysResourcesFilesEntity entity) {
        sysResourcesFilesService.save(entity);
    }
}
