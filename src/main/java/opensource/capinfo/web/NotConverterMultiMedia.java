package opensource.capinfo.web;

import opensource.capinfo.entity.SysResourcesFilesEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName NotConverterMultiMedia 不需要转化多媒体
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/21 0021 8:42
 */
public class NotConverterMultiMedia implements ConverterMultiMedia {

    @Override
    public boolean isConverter() {
        return false;
    }

    @Override
    public String converterTypes() {
        return null;
    }

    @Override
    public void localFileSave(UploaderHandle uploaderHandle) {

    }


    @Override
    public void converter(SysResourcesFilesEntity entity, MultipartFile file) {

    }
}
