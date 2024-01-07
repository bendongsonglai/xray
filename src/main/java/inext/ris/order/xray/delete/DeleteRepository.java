package inext.ris.order.xray.delete;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeleteRepository extends JpaRepository<DeleteModel, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_delete WHERE BUNDLE_ID = :bundle_id", nativeQuery = true)
    BigInteger existsByBundleID(@Param("bundle_id") String bundle_id);
}
