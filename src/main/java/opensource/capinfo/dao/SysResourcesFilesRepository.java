package opensource.capinfo.dao;

import opensource.capinfo.entity.SysResourcesFilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysResourcesFilesRepository extends JpaRepository<SysResourcesFilesEntity,String> {



    List<SysResourcesFilesEntity> findByBusiIdAndFileUniqueCode(String busiId, String fileUniqueCode);

    List<SysResourcesFilesEntity> findByFileUniqueCodeAndFilesDynCode(String fileUniqueCode, String filesDynCode);

    List<SysResourcesFilesEntity> findByFileUniqueCodeAndFilesDynCodeAndFlag(String fileUniqueCode, String filesDynCode,Integer flag);
    @Query(value="select * from sys_resources_files where file_unique_code=?1 and busi_id=?2 and  flag=?3",nativeQuery = true)
    List<SysResourcesFilesEntity> findByFileUniqueCodeAndBusiIdAndFlag(String fileUniqueCode, String busiId,Integer flag);

    List<SysResourcesFilesEntity> findByBusiId(String busiId);

    @Query(value="update sys_resources_files set mime_type=?1,file_size=?2 where id=?3",nativeQuery = true)
    @Modifying
    public void updateOne(String mimeType,String fileSize,String id);
}