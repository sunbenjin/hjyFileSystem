package opensource.capinfo.utils;

import opensource.capinfo.web.SimpleDateCodingRules;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class ImageUtils {
    @Value("${uploader.basePath}")
    private String basePath;

    private static String staticBasePath;
    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @PostConstruct
    public void setBasePathUrl(){
        staticBasePath = this.basePath;
    }
    public static String getStaticBasePath(){
        return staticBasePath;
    }
    /**
     *
     * @param map:需要填入的水印
     * @param srcImgPath：源文件地址
     * @param newImagePath:新生成文件的地址
     * @param degree:角度
     * @param color：水印颜色
     * @param formaName：照片格式
     */
   public static void addWaterMarkToImage(Map<String,String>map,String srcImgPath, String newImagePath, Integer degree, Color color, String formaName,int fontSize,String picType) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            java.awt.Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            double height = buffImg.getHeight() /2;
            double width = buffImg.getWidth()/2;
            if (null != degree) {
                g.rotate(Math.toRadians(degree),  width,height);
            }
            // 5、设置水印文字颜色
             g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(new java.awt.Font("黑体", Font.PLAIN, fontSize));
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
    /*        g.drawString(logoText,  30 , 30);
            g.drawString("北京",30,60);
            g.drawString("时间:",30,90);*/
           Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
           int i =0;

            while(iterator.hasNext()){
                double y = height*2-i-20;
                Double yPosition = new Double(y);
                int intPosition = yPosition.intValue();
                if(StringUtils.equals("location",picType)){
                    i+=30;
                }else{
                    i+=100;
                }
                g.drawString(iterator.next().getValue(),30,intPosition);
            }
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(newImagePath);
            ImageIO.write(buffImg, formaName, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
     Map<String,String> map = new LinkedHashMap<>();
      map.put("longitude&latidude","纬度：39.2342134342,经度：119.1232132131");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      map.put("time","时间："+sdf.format(new Date())+"");
      map.put("address","地点：北京市西城区裕民东路3号京版信息港6层");
      addWaterMarkToImage(map,"E:/2019092558013.jpg","E:/2019092558013.jpg",0,new Color( 0, 0, 0),"JPG",30,"location");
    }
    public static String generatorImg(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        FileOutputStream fos = null;
        try {
            HttpResponse response = client.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();

            SimpleDateCodingRules sdr = new SimpleDateCodingRules();
            String newFileName = sdr.createNewDateBaseFilePath("jpg");
           if(FTPUtils.upload(inputStream,newFileName)){

               return newFileName;
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}