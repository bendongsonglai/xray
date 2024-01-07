package inext.ris.order.xray.sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface IPRepository extends JpaRepository<IP, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_portal_info WHERE IP = :ip AND ENABLED = 1", nativeQuery = true)
    BigInteger existsByIP(@Param("ip") String ip);

    @Query(value="SELECT * FROM xray_portal_info WHERE IP = :ip AND ENABLED = 1", nativeQuery = true)
    IP getPortalByIp(@Param("ip") String ip);
}
