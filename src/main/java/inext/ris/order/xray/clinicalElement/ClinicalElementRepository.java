package inext.ris.order.xray.clinicalElement;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface ClinicalElementRepository extends JpaRepository<ClinicalElement, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_clinical_element WHERE NAME = :name", nativeQuery = true)
    BigInteger existsByName(@Param("name") String name);

    @Query(value="SELECT * FROM xray_clinical_element WHERE ENABLED = 0", nativeQuery = true)
    List<ClinicalElement> clinicalElementsEnabled();

    @Query(value="SELECT * FROM xray_clinical_element WHERE NAME = :name", nativeQuery = true)
    ClinicalElement clinicalElementByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_clinical_element set ENABLED = 1 WHERE NAME = :name", nativeQuery = true)
    void removeClinicalElementByName(@Param("name") String name);
}
