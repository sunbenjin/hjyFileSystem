package opensource.capinfo.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;



import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Map;

public class FreeMarkerUtil {
    private static Configuration freeMarkerConfiguration;

    public static void freeMarkerToWord(Map<String,Object> map, String ftlTemplate, String fileName){
        freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setDefaultEncoding("utf-8");
        PrintWriter pw = null;
        try {
            freeMarkerConfiguration.setDirectoryForTemplateLoading(new File(fileName));
            //configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            Template template = freeMarkerConfiguration.getTemplate(ftlTemplate,"utf-8");
            File outFile = new File(fileName);
            if(!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }
            pw = new PrintWriter(new File(fileName));
            template.process(map,pw);
            pw.flush();
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(pw!=null){
                pw.close();
            }
        }
    }

}
