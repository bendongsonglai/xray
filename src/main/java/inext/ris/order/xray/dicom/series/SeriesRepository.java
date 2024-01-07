package inext.ris.order.xray.dicom.series;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface SeriesRepository extends JpaRepository<SeriesModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_dicom_series WHERE SERIES = :series", nativeQuery = true)
    BigInteger existsBySeries(@Param("series") String series);

    @Query(value="SELECT * FROM xray_dicom_series WHERE STUDY = :study", nativeQuery = true)
    List<SeriesModel> listDICOMByStudy(@Param("study") String study);
}
