package opensource.capinfo.web;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 *
 *  上传的时候使用
 *
 */
public interface UploaderHandle {
    /**
     *
     */
   public boolean handle(MultipartFile mpFile,String fileUrl);



    public boolean handle(File file, String fileUrl);
}
