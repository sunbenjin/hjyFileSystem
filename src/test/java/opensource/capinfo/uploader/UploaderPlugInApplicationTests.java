package opensource.capinfo.uploader;

import opensource.capinfo.GsonUtils;
import opensource.capinfo.entity.FileOutInfo;
import opensource.capinfo.entity.InitialPreview;
import opensource.capinfo.entity.InitialPreviewConfig;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.FTPUtils;
import opensource.capinfo.utils.FileOutBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class UploaderPlugInApplicationTests {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Before
    public void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void test1(){
        try {
            ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/enclosure/fileInput/test"));
            MvcResult mvcResult = perform.andReturn();
            System.out.println(mvcResult.getResponse().getContentAsString());
            //System.out.println(mvcResult.getModelAndView().getView());
            // .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        File file = new File("C:/111.txt");
        SysResourcesFilesEntity fileData = new SysResourcesFilesEntity();
        //fileData

//        FTPUtils.upload(fileData.createNewDateFilePath(),
//                inputStream, rName+"_big."+fileData.getSuffix());


    }




    @Test
    public void test2(){
        //idea 不要鼠标操作
//        FileOutInfo fileOutInfo = new FileOutInfo();
//
//        SysResourcesFilesEntity srf = SysResourcesFilesEntity.builder().fileName("测试").fileSize("102220").fileSuffix("txt").build();
//        FileOutBuilder fileOutBuilder = new FileOutBuilder().builderText(srf);
//        fileOutInfo.setInitialPreviewConfig(fileOutBuilder.getConfigList());
//        fileOutInfo.setInitialPreview(fileOutBuilder.getIpList());
//        fileOutInfo.setAppend(true);

//        System.out.println(GsonUtils.toJson(fileOutInfo));




        /**
         * [
         *  {caption: "Business 1.jpg", size: 762980, url: "$urlD", key: 11},
         *  {previewAsData: false, size: 823782, caption: "Business 2.jpg", url: "$urlD", key: 13},
         *  {caption: "Lorem Ipsum.txt", type: "text", size: 1430, url: "$urlD", key: 12},
         *  {type: "pdf", size: 8000, caption: "PDF Sample.pdf", url: "$urlD", key: 14},
         *  {type: "video", size: 375000, filetype: "video/mp4", caption: "Krajee Sample.mp4", url: "$urlD", key: 15}
         *  ]
         */




    }




    @Test
    public void testUpload() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.fileUpload("/enclosure/fileInput/api/uploader").
                file(new MockMultipartFile("file", "test", "application/ms-excel", new FileInputStream(new File("D:/test.xls")))));
        MvcResult mvcResult = resultActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("==========结果为：==========\n" + result + "\n");
    }

    @Test
    public void testUpolad2() throws Exception {

        //MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        //MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
        //MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"json\": \"someValue\"}".getBytes());
        MockMultipartFile firstFile = new MockMultipartFile("file", "1.jpg", "application/x-jpg", new FileInputStream(new File("D:/图片/1.jpg")));
        MockMultipartFile secondFile = new MockMultipartFile("file", "2.jpg", "application/x-jpg", new FileInputStream(new File("D:/图片/2.jpg")));
        MockMultipartFile threeFile = new MockMultipartFile("file", "3.jpg", "application/x-jpg", new FileInputStream(new File("D:/图片/3.jpg")));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/enclosure/fileInput/api/uploader")
                .file(firstFile)
                .file(secondFile).file(threeFile)
                .param("fileUniqueCode", "123")
                .param("tableName", "test1")
                .param("filesDynCode", "gdgsdgd")).andReturn();


        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);

    }



}
