package inext.ris.order.xray.referrer;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReferrerRepository extends JpaRepository<ReferrerModel, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_referrer WHERE PRACTITIONER_ID = :prac", nativeQuery = true)
    BigInteger existsByPrac(@Param("prac") String prac);
    @Query(value="SELECT * FROM xray_referrer WHERE PRACTITIONER_ID = :prac", nativeQuery = true)
    ReferrerModel getReferrerByPrac(@Param("prac") String prac);

    @Query(value="SELECT * FROM xray_referrer WHERE REFERRER_ID = :ref", nativeQuery = true)
    ReferrerModel getReferrerByRefID(@Param("ref") String ref);
    @Query(value="SELECT COUNT(id) > 0 FROM xray_referrer WHERE PREFIX = :prefix AND PREFIX is not null", nativeQuery = true)
    BigInteger existsByPrefix(@Param("prefix") String prefix);
    @Query(value="SELECT * FROM xray_referrer WHERE PREFIX = :prefix AND PREFIX is not null", nativeQuery = true)
    ReferrerModel getReferrerByPrefix(@Param("prefix") String prefix);
}
