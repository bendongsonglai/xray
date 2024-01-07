package inext.ris.order.xray.aes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AESRepository extends JpaRepository<AES, Long> {
    @Query(value="SELECT * FROM xray_aes  WHERE STATUS = 'ENABLE'", nativeQuery = true)
    AES getAes();
}
