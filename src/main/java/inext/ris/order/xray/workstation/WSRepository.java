package inext.ris.order.xray.workstation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface WSRepository extends JpaRepository<WSModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_workstation WHERE ID = :ID AND AETITLE IS NOT NULL", nativeQuery = true)
    BigInteger existsByTypeID(@Param("ID") Integer ID);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_workstation WHERE AETITLE = :aetitle AND AETITLE IS NOT NULL", nativeQuery = true)
    BigInteger existsByAe(@Param("aetitle") String  aetitle);

    @Query(value="SELECT * FROM xray_workstation WHERE ID = :ID AND AETITLE IS NOT NULL", nativeQuery = true)
    WSModel getWorkstationByTypeID(@Param("ID") Integer ID);

    @Query(value="SELECT * FROM xray_workstation WHERE AETITLE = :aetitle", nativeQuery = true)
    WSModel getWorkstationByAe(@Param("aetitle") String  aetitle);
}
