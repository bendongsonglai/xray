package inext.ris.order.xray.code;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CodeRepository extends JpaRepository<CodeModel, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_code WHERE XRAY_CODE = :xraycode", nativeQuery = true)
    BigInteger existsByXrayCode(@Param("xraycode") String xraycode);
    
    @Query(value="SELECT * FROM xray_code WHERE XRAY_CODE = :xraycode", nativeQuery = true)
    CodeModel getCodeByXrayCode(@Param("xraycode") String xraycode);
    
    @Query(value="SELECT * FROM xray_code WHERE XRAY_TYPE_CODE = :xraytypecode", nativeQuery = true)
    List<CodeModel> getListCodeByXrayTypeCode(@Param("xraytypecode") String xraytypecode);
    
    @Query(value="SELECT COUNT(id) > 0 FROM xray_code WHERE DESCRIPTION = :description AND XRAY_TYPE_CODE = :modality", nativeQuery = true)
    BigInteger existsByDescription(@Param("description") String description, @Param("modality") String modality);
    
    @Query(value="SELECT COUNT(id) > 0 FROM xray_code WHERE XRAY_CODE = :xraycode AND XRAY_TYPE_CODE = :modality", nativeQuery = true)
    BigInteger existsByXrayCode(@Param("xraycode") String xraycode, @Param("modality") String modality);
    
    @Query(value="SELECT * FROM xray_code WHERE DESCRIPTION = :description AND XRAY_TYPE_CODE = :modality", nativeQuery = true)
    CodeModel getCodeByDescription(@Param("description") String description, @Param("modality") String modality);
    
    @Query(value="SELECT * FROM xray_code WHERE XRAY_CODE = :xraycode AND XRAY_TYPE_CODE = :modality", nativeQuery = true)
    CodeModel getCodeByXrayCodeE(@Param("xraycode") String xraycode, @Param("modality") String modality);
}
