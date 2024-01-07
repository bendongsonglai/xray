package inext.ris.order.xray.fhir1;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FHIRRepository1 extends JpaRepository<FHIRModel1, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_fhir1 WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    BigInteger existsByACC(@Param("acc") String acc);
    
    @Query(value="SELECT COUNT(id) > 0 FROM xray_fhir1 WHERE BUNDLE_ID = :bunlde", nativeQuery = true)
    BigInteger existsByBundle(@Param("bunlde") String bunlde);
    
    @Query(value="SELECT * FROM xray_fhir1 WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    FHIRModel1 getFHIRByAccesion(@Param("acc") String acc);
    
    @Query(value="SELECT * FROM xray_fhir1 WHERE BUNDLE_ID = :bundle", nativeQuery = true)
    FHIRModel1 getFHIRByBundle(@Param("bundle") String bundle);
}
