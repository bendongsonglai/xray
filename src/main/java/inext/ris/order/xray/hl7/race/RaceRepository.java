package inext.ris.order.xray.hl7.race;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RaceRepository extends JpaRepository<Race, Long> {
    @Query(value="SELECT * FROM xray_hl7_terminology_race WHERE `DEFAULT` = 1", nativeQuery = true)
    Race FindRaceDefault();
}
