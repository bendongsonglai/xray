package inext.ris.order.xray.report;

import java.math.BigInteger;
import java.util.Date;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<ReportModel, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_report WHERE ACCESSION = :acc AND STATUS_REPORT IS NULL", nativeQuery = true)
    BigInteger existsByAccession(@Param("acc") String acc);
    
    @Query(value="SELECT * FROM xray_report WHERE ACCESSION = :acc", nativeQuery = true)
    ReportModel getReportByAcc(@Param("acc") String acc);
    
    @Modifying
    @Transactional
    @Query(value="UPDATE xray_report set DICTATE_BY = :dictate_by, APPROVE_BY = :approve_by, DESCRIBEREPORT = :describereport, CONCLUSION = :conclusion, APPROVE_DATE = :date, APPROVE_TIME = :time  WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateReportByAccession(@Param("dictate_by") String dictate_by, @Param("approve_by") String approve_by, @Param("describereport") String describereport,
                                 @Param("conclusion") String conclusion, @Param("date") Date date, @Param("time") Date time, @Param("accNo") String accNo);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_report set ACCESSION = :newAcc, STATUS_REPORT = 'CANCEL' WHERE ACCESSION = :accNo", nativeQuery = true)
    void deleteReportByAccession(@Param("newAcc") String newAcc, @Param("accNo") String accNo);
}
