package opensource.capinfo.web;

import opensource.capinfo.entity.FileInputParams;
import opensource.capinfo.entity.FileOutUploader;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.ResultData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName UploaderStrategy
 * @Description TODO 封装上传的策略和方法
 * @Author 消魂钉
 * @Date 6/13 0013 8:15
 */
public interface UploaderStrategy {

    public FileOutUploader init(List<SysResourcesFilesEntity> entityList);

    public FileOutUploader uploadFile(HttpServletRequest request);

    public FileOutUploader uploadFileByMultipartFile(MultipartFile[] files);

    public FileOutUploader uploadFileByURL(String url);
    public FileOutUploader loadFile(List<SysResourcesFilesEntity> fileList);

    /**
     * 点击保存的时候，把数据改成带上数据的业务表id，delflag也改成0
     * @param fileList
     * @return
     */
    public ResultData updateFileEntity(List<SysResourcesFilesEntity> fileList, String busiId);


}
