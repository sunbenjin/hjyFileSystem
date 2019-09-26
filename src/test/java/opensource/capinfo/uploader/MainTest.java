package opensource.capinfo.uploader;

import opensource.capinfo.entity.InitialPreviewConfig;
import org.apache.commons.lang3.EnumUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName MainTest
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/12 0012 23:10
 */
@RunWith(SpringRunner.class)
public class MainTest {



    @Test
    public void test1(){
        System.out.println(getFileType());

//        enumList.forEach(v->{
//            System.out.println(v.getSuffix());
//        });

    }

    private String getFileType() {
        List<InitialPreviewConfig.InitialPreviewFileType> enumList =
                EnumUtils.getEnumList(InitialPreviewConfig.InitialPreviewFileType.class);
        Optional<InitialPreviewConfig.InitialPreviewFileType> doc = enumList.stream().filter(v -> {
            if (v.getSuffix().contains("zip12")) {
                return true;
            }
            return false;
        }).findFirst();
        if (doc.isPresent()){
           return doc.get().getType();
        }
        return "";
    }


    @Test
    public void test3(){
        //File file = new File("C:/workproject/idea/uploaderPlugin/src/main/resources/default/ffmpeg/temp.avi");
        //C:\Users\李陶琳\WebstormProjects\WebDemo\index.html
        //C:\Users\李陶琳\WebstormProjects\WebDemo\index.html

        //D:\WEB课堂\05.javascript高级(共110多集)\day3\03视频\03原型及原型链.avi
        File file = new File("C:/workproject/idea/uploaderPlugin/src/main/resources/default/ffmpeg/temp.avi");
        try {

            String mimeType = Files.probeContentType(Paths.get(file.toURI()));
            System.out.println(mimeType);
//            MagicMatch magicMatch = Magic.getMagicMatch(file, false);
//            System.out.println(magicMatch.getMimeType());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //InitialPreviewFileType


    @Test
    public void test4(){
        System.out.println("ppt".matches("(ppt|pptx)$"));


    }



}
