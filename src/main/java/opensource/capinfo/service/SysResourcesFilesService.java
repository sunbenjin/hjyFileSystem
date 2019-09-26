package opensource.capinfo.service;

import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.dao.FileContentTypeRepository;
import opensource.capinfo.dao.SysResourcesFilesRepository;
import opensource.capinfo.entity.FileContentType;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.FTPUtils;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.beans.Transient;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @ClassName SysResourcesFilesService
 * @Description TODO
 * @Author 消魂钉
 * @Date 7/2 0002 0:47
 */
@Service
@Lazy(false)
@EnableScheduling
public class SysResourcesFilesService {
    @Autowired
    private SysResourcesFilesRepository sysResourcesFilesRepository;

    @Autowired
    private ApplicationProperties props;
    @Autowired
    private FileContentTypeRepository fileContentTypeRepository;
   /* @Autowired
    private DocumentConverter documentConverter;*/
    @Transactional(readOnly = false)
    public void save(SysResourcesFilesEntity sysResourcesFilesEntity) {
        sysResourcesFilesRepository.save(sysResourcesFilesEntity);
    }
    @Transactional(readOnly = false)
    public  void wordToPdf(String wordPath,String destFile){
        File inputFile = null;
        File outFile = null;
        FileInputStream in = null;
        try {
            inputFile = new File(wordPath);
            if(!inputFile.exists()){
                return;
            }

            outFile = new File(destFile);
            if(!outFile.exists()){
                outFile.getParentFile().mkdirs();
            }
            in = new FileInputStream(inputFile);
            //  documentConverter.convert(inputFile,outFile);

            //documentConverter.convert(in).as(DefaultDocumentFormatRegistry.DOC).to(outFile).execute();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * @param key
     */
    @Transactional(readOnly = false)
    public boolean delete(String key) {
        //上传就上传上去吧，删除关联就可以了。
        if (StringUtils.isNotBlank(key)) {
            SysResourcesFilesEntity entity = SysResourcesFilesEntity.builder().id(key).build();
            sysResourcesFilesRepository.delete(entity);
            return true;
        }
        return false;
    }


    public SysResourcesFilesEntity findById(String fileId) {
        return sysResourcesFilesRepository.getOne(fileId);
    }

    @Transactional(readOnly = true)
    public List<SysResourcesFilesEntity> findByBusiIdAndFileUniqueCode(String busiId, String fileUniqueCode) {
        return sysResourcesFilesRepository.findByBusiIdAndFileUniqueCode(busiId, fileUniqueCode);
    }
    public List<SysResourcesFilesEntity> findByBusiId(String busiId) {
        return sysResourcesFilesRepository.findByBusiId(busiId);
    }

    public List<SysResourcesFilesEntity> findByFileUniqueCodeAndFilesDynCode(String fileUniqueCode, String filesDynCode) {
        return sysResourcesFilesRepository.findByFileUniqueCodeAndFilesDynCode(fileUniqueCode, filesDynCode);
    }
    public List<SysResourcesFilesEntity> findByFileUniqueCodeAndFilesDynCodeAndFlag(String fileUniqueCode, String filesDynCode,Integer flag) {
        return sysResourcesFilesRepository.findByFileUniqueCodeAndFilesDynCodeAndFlag(fileUniqueCode, filesDynCode,flag);
    }
    public List<SysResourcesFilesEntity> findByFileUniqueCodeAndBusiIdAndFlag(String fileUniqueCode, String busiId,Integer flag) {
        return sysResourcesFilesRepository.findByFileUniqueCodeAndBusiIdAndFlag(fileUniqueCode,busiId,flag);
    }
    @Transactional(readOnly = false)
    @Scheduled(cron = "0 3 10 * * *")
    public void autoUpdateSysResourcesFilesOnce() {
        List<SysResourcesFilesEntity> list = sysResourcesFilesRepository.findAll();
        System.out.println(list.size()+" list size");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SysResourcesFilesEntity entity = list.get(i);
                String fileUrl = entity.getFileUrl();
                File file = new File(props.getBasePath() + fileUrl);
                if (file.exists() && file.isFile()) {
                  //  entity.setFileSize(file.length() + "");
                    String fileName = file.getName();
                    String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    List<FileContentType> typeList = fileContentTypeRepository.findMineTypeByFileSuffix(StringUtils.isNotBlank(fileSuffix) ? fileSuffix.toLowerCase() : ".*");
                    String mimeType = "";
                    if (typeList != null && typeList.size() > 0) {
                        mimeType = typeList.get(0).getMimeType();
                    }
                  // entity.setMimeType(mimeType);
                    System.out.println(entity.getId()+" :"+file.length()+" :"+mimeType );
                    //sysResourcesFilesRepository.save(entity);
                    sysResourcesFilesRepository.updateOne(mimeType,file.length()+"",entity.getId());
                }
            }
        }
    }

    public static void main(String[] args) {
        File file = new File("D:\\Download/myArrayList.zip");
        String fileName = file.getName();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        System.out.println(fileSuffix);
    }

}
