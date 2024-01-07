package inext.ris.order.xray.hl7.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value="SELECT * FROM xray_hl7_terminology_country WHERE `DEFAULT` = 1", nativeQuery = true)
    Country FindCountryDefault();
}
