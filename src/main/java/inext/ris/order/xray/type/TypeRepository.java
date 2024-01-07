package inext.ris.order.xray.type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TypeRepository extends JpaRepository<TypeModel, Long>{
    @Query(value="SELECT * FROM xray_type WHERE XRAY_TYPE_CODE = :type", nativeQuery = true)
    TypeModel FindXrayType(@Param("type") String type);
}
