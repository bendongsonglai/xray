package inext.ris.order.xray.pacs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface PacsRepository extends JpaRepository<PacsModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_pacs WHERE AETITLE = :aetitle", nativeQuery = true)
    BigInteger existsByAetitle(@Param("aetitle") String aetitle);
}
