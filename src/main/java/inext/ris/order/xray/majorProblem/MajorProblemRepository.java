package inext.ris.order.xray.majorProblem;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface MajorProblemRepository extends JpaRepository<MajorProblem, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_major_problem WHERE NAME = :name", nativeQuery = true)
    BigInteger existsByName(@Param("name") String name);

    @Query(value="SELECT * FROM xray_major_problem WHERE ENABLED = 0", nativeQuery = true)
    List<MajorProblem> majorProblemsEnabled();

    @Query(value="SELECT * FROM xray_major_problem WHERE NAME = :name", nativeQuery = true)
    MajorProblem majorProblemByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_major_problem set ENABLED = 1 WHERE NAME = :name", nativeQuery = true)
    void removeMajorProblemByName(@Param("name") String name);
}
