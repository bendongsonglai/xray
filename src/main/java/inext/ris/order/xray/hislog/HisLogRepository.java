package inext.ris.order.xray.hislog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HisLogRepository extends JpaRepository<HisLog, Long> {
}
