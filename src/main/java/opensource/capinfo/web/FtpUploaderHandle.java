package opensource.capinfo.web;

import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.FTPUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName FtpUploaderHandle
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/27 0027 15:39
 */
public class FtpUploaderHandle implements UploaderHandle {
    private Lock lock = new ReentrantLock();
    /**
     *
     * @param mpFile
     * @return 文件处理
     */
    @Override
    public boolean handle(MultipartFile mpFile, String fileUrl) {
        boolean upload = false;
        try {
            lock.lock();
            upload = FTPUtils.upload(mpFile.getInputStream(),fileUrl);
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upload;
    }


    @Override
    public boolean handle(File file, String fileUrl) {
        boolean upload = false;
        try {
            lock.lock();
            upload = FTPUtils.upload(file,fileUrl);
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upload;
    }

    public static void main(String[] args) {
     // short s=2; s+=1;
        System.out.println(0.1*3);
    }
}
