package inext.ris.order.xray.hapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HapiRepository extends JpaRepository<Hapi, Long> {
    @Query(value="SELECT * FROM xray_hapi_server", nativeQuery = true)
    Hapi getHapi();
}
