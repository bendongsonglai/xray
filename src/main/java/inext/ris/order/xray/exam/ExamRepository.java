package inext.ris.order.xray.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_exam WHERE ACCESSION = :acc", nativeQuery = true)
    BigInteger existsByAccNo(@Param("acc") String acc);

    @Query(value="SELECT * FROM xray_exam WHERE ACCESSION = :acc", nativeQuery = true)
    Exam getExamByAcc(@Param("acc") String acc);
}
