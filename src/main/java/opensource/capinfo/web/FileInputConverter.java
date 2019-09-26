package opensource.capinfo.web;

import opensource.capinfo.GsonUtils;
import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.entity.FileOutInfo;
import opensource.capinfo.entity.FileOutUploader;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.FileOutBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName FileInputConverter
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/27 0027 17:04
 */
public class FileInputConverter implements JsonConverter {

    private static ApplicationProperties props;

    public static void setProps(ApplicationProperties props) {
        FileInputConverter.props = props;
    }

    @Override
    public FileOutUploader outData(List<SysResourcesFilesEntity> list, ConverterToMp4 isConverterMp4) {

        FileOutBuilder fileOutBuilder = new FileOutBuilder();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(srfe -> {
                //外部资源服务器的绝对路
                srfe.setFileUrl(props.getDisplayPath()+srfe.getFileUrl());
                if(isConverterMp4.isConverter()){
                    if(StringUtils.contains(isConverterMp4.converterTypes(), srfe.getFileSuffix())) {
                        String tempPath = srfe.getFileUrl().replaceAll(srfe.getFileSuffix(), "mp4");
                        srfe.setFileUrl(tempPath);
                        srfe.setMimeType("video/mp4");
                    }
                }
                fileOutBuilder.builder(srfe);
            });
        }
        FileOutInfo fileOutInfo = new FileOutInfo();
        fileOutInfo.setInitialPreviewConfig(fileOutBuilder.getConfigList());
        fileOutInfo.setInitialPreview(fileOutBuilder.getIpList());
        fileOutInfo.setAppend(true);
        return fileOutInfo;
    }
}
