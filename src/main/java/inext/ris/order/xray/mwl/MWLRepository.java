package inext.ris.order.xray.mwl;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface MWLRepository extends JpaRepository<MWLModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_hl7 WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    BigInteger existsByACC(@Param("acc") String acc);

    @Query(value="SELECT * FROM xray_hl7 WHERE STATUS = :status", nativeQuery = true)
    List<MWLModel> ListsMWL(@Param("status") String status);

    @Query(value="SELECT * FROM xray_hl7 WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    MWLModel getMWLByAcc(@Param("acc") String acc);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_hl7 set STATUS = :status WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    MWLModel updateMWLByAcc(@Param("status") String status, @Param("acc") String acc);
}
