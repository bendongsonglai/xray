package inext.ris.order.xray.doituong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface DoituongRepository extends JpaRepository<DoituongModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_doituong WHERE TYPE_ID = :type_id", nativeQuery = true)
    BigInteger existsByTypeID(@Param("type_id") String type_id);

    @Query(value="SELECT * FROM xray_doituong WHERE TYPE_ID = :type_id", nativeQuery = true)
    DoituongModel getDoituongByTypeID(@Param("type_id") String type_id);
}
