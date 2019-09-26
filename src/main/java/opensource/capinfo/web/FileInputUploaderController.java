package opensource.capinfo.web;

import freemarker.ext.servlet.FreemarkerServlet;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import opensource.capinfo.GsonUtils;
import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.entity.*;
import opensource.capinfo.service.SysResourcesFilesService;
import opensource.capinfo.utils.*;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/**
 *
 * /enclosure/fileInput/beetl
 */
@RestController
@Slf4j
@RequestMapping("/enclosure/fileInput")
@Api(description = "FileInput插件接口")
public class FileInputUploaderController {

    @Autowired
    private SysResourcesFilesService sysResourcesFilesService;

    @Value("${localaddress}")
    private String localaddress;
    @Value("${server.port}")
    private String serverPort;

    @Value("${uploader.basePath}")
    private String basePath;
    /**
     * 测试是否连通可用
     *
     * @return
     */
    //@ApiOperation(value = "测试", notes = "测试是否连通可用")
    @GetMapping(value = "/test")
    public String test() {
        SysResourcesFilesEntity entity = SysResourcesFilesEntity.builder().id("李陶琳").busiId("测试一下").build();

        sysResourcesFilesService.save(entity);

        return GsonUtils.toJson(entity);
    }

    //@ApiOperation(value = "测试", notes = "测试beetl是否OK")
    @GetMapping(value = "/beetl")
    public String api(){
        return "/demo";
    }



    //@ApiOperation(value = "上传文件", notes = "上传下载组件")
    @PostMapping(value = "/uploader")
    public FileOutUploader uploader(FileInputParams fileInputParams,HttpServletRequest request) {
        //文件上传
        FileInputUploaderStrategy strategy =
                new FileInputUploaderStrategy(
                        fileInputParams,new FtpUploaderHandle(),
                        new SimpleDateCodingRules(),
                        new MySqlUploaderStorage(),
                        new FileInputConverter(),new ConverterToMp4());
        FileOutUploader fileOutUploader = strategy.uploadFile(request);
        System.out.println(GsonUtils.toJson(fileOutUploader));
        return fileOutUploader;
    }

    /**
     * 上传文件接口
     * @param
     * @param request
     * @return
     *
     * (@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，未保存数据前的上传，需传入fileUniqueCode组件标识、tableName对应业务表表名、filesDynCode页面随机码以及附件",required=true) FileInputParams fileInputParams
     */
    @ApiOperation(value = "上传文件", notes = "上传下载组件")
    @PostMapping(value = "/api/uploader",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileUniqueCode", value = "fileUniqueCode", required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "tableName", value = "tableName", required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "filesDynCode", value = "filesDynCode", required = true,dataType = "String",paramType = "query")
    })
    public ResultData uploaderApi(@ApiParam(name="fileUniqueCode") String fileUniqueCode, @ApiParam(name="tableName") String tableName, @ApiParam(name="filesDynCode") String filesDynCode,  @ApiParam(value="附件",required=true) MultipartFile[] file , HttpServletRequest request) {
        MultipartHttpServletRequest requestFiles = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = requestFiles.getFileMap();
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        if (!CollectionUtils.isEmpty(fileMap)){
            fileMap.forEach((k,value)->{
                files.add(value);
            });
        }
        ResultData result = new ResultData();
        FileInputParams fileInputParams = new FileInputParams();
        fileInputParams.setFileUniqueCode(fileUniqueCode);
        fileInputParams.setTableName(tableName);
        fileInputParams.setFilesDynCode(filesDynCode);
        fileInputParams.setFlag(0);
        try {
            //文件上传
            FileInputUploaderStrategy strategy =
                    new FileInputUploaderStrategy(
                            fileInputParams,new FtpUploaderHandle(),
                            new SimpleDateCodingRules(),
                            new MySqlUploaderStorage(),
                            new FileInputConverter(),new ConverterToMp4());
            //FileOutUploader fileOutUploader = strategy.uploadFile(request);
            FileOutUploader fileOutUploader = strategy.uploadFileByMultipartFile(files.toArray(new MultipartFile[]{}));
            result.setMsg("文件上传成功");
            result.setFlag(true);
            result.setData(fileOutUploader);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("上传文件失败");
        }
    }







    //@ApiOperation(value = "点击保存修改对应附件", notes = "点击保存修改对应附件")
    @PostMapping(value = "/updateFile")
    public ResultData updateFile(FileInputParams fileInputParams,HttpServletRequest request) {

        List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByFileUniqueCodeAndFilesDynCode(fileInputParams.getFileUniqueCode(), fileInputParams.getFilesDynCode());
        FileInputUploaderStrategy strategy =
                new FileInputUploaderStrategy(
                        fileInputParams,new FtpUploaderHandle(),
                        new SimpleDateCodingRules(),
                        new MySqlUploaderStorage(),
                        new FileInputConverter(),new ConverterToMp4());
        ResultData resultData = strategy.updateFileEntity(list, fileInputParams.getBusiId());

        return resultData;
    }

    @ApiOperation(value = "点击保存修改对应附件", notes = "点击保存修改对应附件")
    @PostMapping(value = "/api/updateFile")
    public ResultData updateFileApi(@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，保存数据后调用保存对应附件，应传入busiId业务表数据id、fileUniqueCode组件标识、filesDynCode页面随机码",required=true) FileInputParams fileInputParams,HttpServletRequest request) {

         ResultData resultData = null;
        try {
            List<SysResourcesFilesEntity> positonList = sysResourcesFilesService.findByFileUniqueCodeAndBusiIdAndFlag("check_location_pic",fileInputParams.getBusiId(),1);
            if(!CollectionUtils.isEmpty(positonList)){
               for(SysResourcesFilesEntity entity:positonList){
                   entity.setDelFlag("1");
                   sysResourcesFilesService.save(entity);

               }
            }
            List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByFileUniqueCodeAndFilesDynCodeAndFlag(fileInputParams.getFileUniqueCode(), fileInputParams.getFilesDynCode(),0);
            FileInputUploaderStrategy strategy =
                    new FileInputUploaderStrategy(
                            fileInputParams,new FtpUploaderHandle(),
                            new SimpleDateCodingRules(),
                            new MySqlUploaderStorage(),
                            new FileInputConverter(),new ConverterToMp4());
            resultData = strategy.updateFileEntity(list, fileInputParams.getBusiId());
            Map<String,String> map = new LinkedHashMap<>();
            map.put("longitude&latidude","经度："+fileInputParams.getLongitude()+"  纬度："+fileInputParams.getLatitude());
            map.put("time","时间："+ DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            map.put("location:","地点："+fileInputParams.getAddress());
            if(!CollectionUtils.isEmpty(list)){
                for(SysResourcesFilesEntity entity:list){
                    String picType = entity.getFileUniqueCode();
                    ImageUtils.addWaterMarkToImage(map,basePath+entity.getFileUrl(),basePath+entity.getFileUrl(),0,(StringUtils.equals(picType,"check_location_pic")?new Color( 0, 0, 0):new Color( 255, 255, 255)),entity.getFileSuffix(),(StringUtils.equals(picType,"check_location_pic")?10:100),(StringUtils.equals(picType,"check_location_pic")?"location":"photo"));
                }
            }

            return resultData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("保存附件数据失败");
        }
    }






    @ApiOperation(value = "回显界面", notes = "回显界面")
    @PostMapping(value = "/init")
    public FileOutUploader init(FileInputParams fileInputParams,HttpServletRequest request) {

        //文件上传
        FileInputUploaderStrategy strategy =
                new FileInputUploaderStrategy(
                        fileInputParams,new FtpUploaderHandle(),
                        new SimpleDateCodingRules(),
                        new MySqlUploaderStorage(),
                        new FileInputConverter(),new ConverterToMp4());
        List<SysResourcesFilesEntity> newList =
                sysResourcesFilesService.findByBusiIdAndFileUniqueCode(fileInputParams.getBusiId(),fileInputParams.getFileUniqueCode());


        return strategy.init(newList);
    }


    @ApiOperation(value = "回显对应附件", notes = "回显对应附件")
    @PostMapping(value = "/api/init")
    public ResultData initApi(@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，回显对应数据对应上传组件的附件，需传入busiId业务表数据id、fileUniqueCode组件标识",required = true) FileInputParams fileInputParams,HttpServletRequest request) {

        ResultData result = new ResultData();
        try {
            //文件上传
            FileInputUploaderStrategy strategy =
                    new FileInputUploaderStrategy(
                            fileInputParams,new FtpUploaderHandle(),
                            new SimpleDateCodingRules(),
                            new MySqlUploaderStorage(),
                            new FileInputConverter(),new ConverterToMp4());
            List<SysResourcesFilesEntity> newList = new ArrayList<>();
            newList= sysResourcesFilesService.findByBusiIdAndFileUniqueCode(fileInputParams.getBusiId(),fileInputParams.getFileUniqueCode());
            FileOutUploader init = strategy.init(newList);
            result.setFlag(true);
            result.setCode(200);
            result.setMsg("获取附件数据成功");
            result.setData(init);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("获取附件数据失败");
        }
    }




    //@ApiOperation(value = "删除文件",notes = "删除文件")
    @PostMapping(value = "/delete")
    public String delete(String key){
        System.out.println(key);
        /**
         * 通过主键删除文件
         */
        if(sysResourcesFilesService.delete(key)){
            return "{\"message\":\"文件删除成功\"}";
        }
        return "{\"message\":\"文件删除失败\"}";
    }

    @ApiOperation(value = "删除文件",notes = "删除文件")
    @PostMapping(value = "/api/delete")
    public ResultData deleteApi(@RequestBody @ApiParam(name="附件主键",value="附件主键",required = true) Map<String,Object> map) {

        try {
            String key = map.get("key").toString();
            if(sysResourcesFilesService.delete(key)){
                return ResultData.sucess("文件删除成功");
            } else {
                return ResultData.error("文件删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("文件删除失败");
        }
    }



    @ApiOperation(value = "下载文件",notes = "下载文件")
    @GetMapping(value = "/download")
    public void download(String key, HttpServletResponse response){

        SysResourcesFilesEntity entity = sysResourcesFilesService.findById(key);
        OutputStream outputStream = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + entity.getFileAllName() + "\"");
            outputStream = new BufferedOutputStream(
                    response.getOutputStream());
            InputStream inputStream = FTPUtils.getInputStream(entity.getFileUrl(), 1000000L);
            byte data[] = new byte[1024];
            while (inputStream.read(data, 0, 1024) >= 0) {
                outputStream.write(data);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            if(outputStream!=null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @ApiOperation(value = "busiId", notes = "通过busiId查找附件list")
    @GetMapping(value = "/api/findByBusiId")
    public ResultData findByBusiId(HttpServletRequest request,String busiId) {
        try {
           if(StringUtils.isNotBlank(busiId)){
               List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByBusiId(busiId);
               List<SysResourcesFilesEntity> entities = new ArrayList<>();
               if(!CollectionUtils.isEmpty(list)){
                  for(SysResourcesFilesEntity entity:list){
                      entity.setFileUrl(localaddress+":"+serverPort+"/image/"+entity.getFileUrl());
                      entities.add(entity);
                  }
               }
               return ResultData.sucess("ok",entities);
           }else{
               return ResultData.error("fail");
           }

        }catch (Exception e){

            return ResultData.error("fail");

        }
    }
    @ApiOperation(value = "busiId", notes = "通过busiId查找附件list")
    @GetMapping(value = "/api/findByBusiIdAndUniqueCode")
    public ResultData findByBusiIdAndUniqueCode(HttpServletRequest request,FileInputParams fileInputParams) {
        try {
            String busiId = fileInputParams.getBusiId();
            String fileUniqueCode = fileInputParams.getFileUniqueCode();
            if(StringUtils.isNotBlank(busiId)&& StringUtils.isNotBlank(fileUniqueCode)){
                List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByBusiIdAndFileUniqueCode(busiId,fileUniqueCode);
                List<FileUrlEntity> entities = new ArrayList<>();
                if(!CollectionUtils.isEmpty(list)){
                    for(SysResourcesFilesEntity entity:list){
                        FileUrlEntity fileUrlEntity = new FileUrlEntity();
                        fileUrlEntity.setFileUrl(entity.getFileUrl());
                             entities.add(fileUrlEntity);
                    }
                }
                return ResultData.sucess("ok",entities);
            }else{
                return ResultData.error("fail");
            }

        }catch (Exception e){

            return ResultData.error("fail");

        }
    }
    @ApiOperation(value = "生成页面随机码", notes = "生成页面随机码")
    @GetMapping(value = "createFileDynCode")
    public ResultData createFileDynCode() {
        try {
            ResultData result = ResultData.sucess("生成页面随机码成功");
            String dynCode = UUID.randomUUID().toString().replaceAll("-", "");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("dynCode", dynCode);
            result.setData(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("生成页面随机码失败");
        }
    }
    @ApiOperation(value = "地图生成图片",notes = "地图生成图片")
    @PostMapping(value = "/api/generaterImg")
    public ResultData generaterImg(@RequestBody @ApiParam(name="附件参数",value="附件参数",required = true) Map<String,Object> map) {

        try {
            ResultData result = new ResultData();

            String url = map.get("url").toString();
           String path =  ImageUtils.generatorImg(url);

            SysResourcesFilesEntity sysResourcesFilesEntity = new SysResourcesFilesEntity();
            sysResourcesFilesEntity.setFileUniqueCode(map.get("fileUniqueCode").toString());
            sysResourcesFilesEntity.setFileUrl(path);
            sysResourcesFilesEntity.setFilesDynCode(map.get("filesDynCode").toString());
            sysResourcesFilesEntity.setFlag(0);

            sysResourcesFilesEntity.setFileSuffix("jpg");
            sysResourcesFilesEntity.setId(UUID.randomUUID()+"");
           sysResourcesFilesEntity.setMimeType("image/jpg");
           sysResourcesFilesEntity.setSysId(map.get("fileUniqueCode").toString());
           sysResourcesFilesEntity.setFileType("image");
            sysResourcesFilesEntity.setFileName("地图点位点位截图");
           sysResourcesFilesService.save(sysResourcesFilesEntity);

        return   ResultData.sucess("ok",localaddress+":"+serverPort+"/image/"+path);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("文件删除失败");

        }
    }
}

