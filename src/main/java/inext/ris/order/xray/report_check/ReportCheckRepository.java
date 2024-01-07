package inext.ris.order.xray.report_check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface ReportCheckRepository extends JpaRepository<ReportCheck, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_report_check WHERE accession = :accession", nativeQuery = true)
    BigInteger existsByAccNo(@Param("accession") String accession);
}
