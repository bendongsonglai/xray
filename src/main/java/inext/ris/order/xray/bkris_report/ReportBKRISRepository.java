package inext.ris.order.xray.bkris_report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface ReportBKRISRepository extends JpaRepository<ReportBKRIS, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_report WHERE ACCESSION = :acc", nativeQuery = true)
    BigInteger existsByACC(@Param("acc") String acc);
}
