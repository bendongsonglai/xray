package inext.ris.order.xray.dicom.instance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface ObjectDicomRepository extends JpaRepository<ObjectDicom, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_dicom_instances WHERE OBJECT = :object", nativeQuery = true)
    BigInteger existsByObject(@Param("object") String object);

    @Query(value="SELECT * FROM xray_dicom_instances WHERE SERIES = :series", nativeQuery = true)
    List<ObjectDicom> listDICOMBySeries(@Param("series") String series);
}
