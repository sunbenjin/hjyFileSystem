package opensource.capinfo.web;

import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.entity.*;
import opensource.capinfo.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName FileInputUploaderStrategy
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/13 0013 8:44
 */
public class FileInputUploaderStrategy implements UploaderStrategy {


    @Autowired
    protected ApplicationProperties props;

    @Value("${uploader.basePath}")
    private String basePath;

    protected FileInputParams fileInputParams = new FileInputParams();

    protected WriteForm writeForm = new WriteForm();

    /**
     * FileInputUploader 上传下载策略设置
     * @param fileInputParams 页面参数。
     * @param uploaderHandle 使用哪种上传处理方式。
     * @param fileNamingRules 使用哪种名称生成策略。
     * @param uploaderStorage 使用那种数据存储方式。
     * @param jsonConverter 转化成那种json格式进行返回。
     * @param converterMp4 转化成Mp4格式。
     */
    public FileInputUploaderStrategy(FileInputParams fileInputParams,
                                     UploaderHandle uploaderHandle,
                                     FileNamingRules fileNamingRules,
                                     UploaderStorage uploaderStorage,
                                     JsonConverter jsonConverter,
                                     ConverterToMp4 converterMp4) {
        this.fileInputParams = fileInputParams;
        this.uploaderHandle = uploaderHandle;
        this.fileNamingRules = fileNamingRules;
        this.uploaderStorage = uploaderStorage;
        this.jsonConverter = jsonConverter;
        this.converterMp4 = converterMp4;
    }

    public FileInputUploaderStrategy(WriteForm writeForm,
                                     UploaderHandle uploaderHandle,
                                     FileNamingRules fileNamingRules,
                                     UploaderStorage uploaderStorage,
                                     JsonConverter jsonConverter,
                                     ConverterToMp4 converterMp4){
        this.writeForm = writeForm;
        this.uploaderHandle = uploaderHandle;
        this.fileNamingRules = fileNamingRules;
        this.uploaderStorage = uploaderStorage;
        this.jsonConverter = jsonConverter;
        this.converterMp4 = converterMp4;

    }
    /**
     * 上传文件
     */
    protected UploaderHandle uploaderHandle;
    /**
     * 文件命名规则
     */
    protected FileNamingRules fileNamingRules;
    /**
     * 存储器
     */
    protected UploaderStorage uploaderStorage;
    /**
     * json转化器
     */
    protected JsonConverter jsonConverter;

    /**
     * 转化多媒体
     */
    protected ConverterToMp4 converterMp4; //是否转化多媒体


    @Override
    public FileOutUploader init(List<SysResourcesFilesEntity> entityList) {
        return jsonConverter.outData(entityList,converterMp4);
    }

    @Override
    public FileOutUploader uploadFile(HttpServletRequest request) {
        try {
            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                List<SysResourcesFilesEntity> newList = new ArrayList<SysResourcesFilesEntity>();
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    MultipartFile file = multiRequest.getFile(iter.next());
                    SysResourcesFilesEntity entity = simpleUploadFile(file);
                    if(entity==null){
                        throw new RuntimeException("文件转化失败");
                    }
                    entity.setMimeType(file.getContentType());
//                    String mimeType = Files.probeContentType(
//                            Paths.get(file.getResource().getFile().toURI()));
//                    entity.setMimeType(mimeType);
                    if(uploaderHandle.handle(file,entity.getFileUrl())){
                        uploaderStorage.save(entity,fileInputParams);
                        //最后转化
                        if(converterMp4.isConverter()){
                            converterMp4.localFileSave(uploaderHandle);
                            converterMp4.converter(entity,file);
                        }
                    }
                    newList.add(entity);
                }
                return jsonConverter.outData(newList,converterMp4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new FileOutInfo();
        }
        return new FileOutInfo();
    }

    @Override
    public FileOutUploader uploadFileByMultipartFile(MultipartFile[] files) {

        try {
            List<SysResourcesFilesEntity> newList = new ArrayList<SysResourcesFilesEntity>();
            for (MultipartFile file : files) {

                SysResourcesFilesEntity entity = simpleUploadFile(file);
                if(entity==null){
                    throw new RuntimeException("文件转化失败");
                }
                entity.setMimeType(file.getContentType());
                if(uploaderHandle.handle(file,entity.getFileUrl())){
                    uploaderStorage.save(entity,fileInputParams);
                    System.out.println(converterMp4.isConverter());
                    if(converterMp4.isConverter()){
                        converterMp4.localFileSave(uploaderHandle);
                        converterMp4.converter(entity,file);

                    }
                }
                newList.add(entity);
            }
            return jsonConverter.outData(newList,converterMp4);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new FileOutInfo();
        }
    }

    @Override
    public FileOutUploader uploadFileByURL(String newFileName) {
        File filePath = new File(basePath+newFileName) ;
        return null;
    }

    @Override
    public FileOutUploader loadFile(List<SysResourcesFilesEntity> fileList) {
        return null;
    }

    @Override
    public ResultData updateFileEntity(List<SysResourcesFilesEntity> fileList, String busiId) {

        fileList.forEach(sysResourcesFilesEntity -> {
            sysResourcesFilesEntity.setDelFlag("0");
            sysResourcesFilesEntity.setBusiId(busiId);
            sysResourcesFilesEntity.setFlag(1);
            uploaderStorage.updateFileData(sysResourcesFilesEntity);
        });
        return ResultData.sucess("保存完成");
    }


    /**
     * 文件上传封装
     * @param file
     * @return
     */
    private SysResourcesFilesEntity simpleUploadFile(MultipartFile file) {
        if (file!=null) {
            SysResourcesFilesEntity.
                    SysResourcesFilesEntityBuilder builder =
                    SysResourcesFilesEntity.builder();
            String myFileName = file.getOriginalFilename();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在
            if (myFileName.trim() != "") {
                builder.fileSize(file.getSize()+"");
                fileNamingRules.rules(builder,myFileName);
            }
            return builder.build();
        }
        return null;
    }
    private SysResourcesFilesEntity simpleUploadFileByUrl(File file) {
        if (file!=null) {
            SysResourcesFilesEntity.
                    SysResourcesFilesEntityBuilder builder =
                    SysResourcesFilesEntity.builder();
            String myFileName = file.getName();
            //如果名称不为“”,说明该文件存在，否则说明该文件不存在
            if (myFileName.trim() != "") {
                builder.fileSize(file.length()+"");
                fileNamingRules.rules(builder,myFileName);
            }
            return builder.build();
        }
        return null;
    }

}
