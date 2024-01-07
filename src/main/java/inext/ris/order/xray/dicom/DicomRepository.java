package inext.ris.order.xray.dicom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface DicomRepository extends JpaRepository<DicomModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_dicom WHERE ACCESSION = :acc", nativeQuery = true)
    BigInteger existsByAccNo(@Param("acc") String acc);

    @Query(value="SELECT * FROM xray_dicom WHERE ACCESSION = :acc", nativeQuery = true)
    DicomModel getDICOMByAcc(@Param("acc") String acc);

    @Query(value="SELECT * FROM xray_dicom WHERE STUDY = :study", nativeQuery = true)
    DicomModel getDICOMByStudy(@Param("study") String study);

    @Query(value="SELECT * FROM xray_dicom WHERE STUDY_DATE = :from AND STUDY_DATE <= :to ORDER BY STUDY_DATE DESC", nativeQuery = true)
    List<DicomModel> getList(@Param("from") String from, @Param("to") String to);
}
