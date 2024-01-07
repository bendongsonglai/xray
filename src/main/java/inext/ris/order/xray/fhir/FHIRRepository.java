package inext.ris.order.xray.fhir;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FHIRRepository extends JpaRepository<FHIRModel, Long> {
    @Query(value = "SELECT COUNT(id) > 0 FROM xray_fhir WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    BigInteger existsByACC(@Param("acc") String acc);

    @Query(value = "SELECT COUNT(id) > 0 FROM xray_fhir WHERE ACCESSION_NUMBER = :acc and STATUS = '1'", nativeQuery = true)
    BigInteger existsReportByACC(@Param("acc") String acc);

    @Query(value = "SELECT COUNT(id) > 0 FROM xray_fhir WHERE BUNDLE_ID = :bundle_id and STATUS = '1'", nativeQuery = true)
    BigInteger existsReportByBundleReport(@Param("bundle_id") String bundle_id);

    @Query(value = "SELECT COUNT(id) > 0 FROM xray_fhir WHERE SERVICE_ID = :imaging", nativeQuery = true)
    BigInteger existsByImaging(@Param("imaging") String imaging);

    @Query(value = "SELECT BUNDLE_ID FROM (SELECT * FROM xray_fhir where status='0' ORDER BY id DESC LIMIT 150) AS BUNDLE_ID ORDER BY id ASC", nativeQuery = true)
    List<String > listLastOrder();
    @Query(value = "SELECT * FROM xray_fhir WHERE ACCESSION_NUMBER = :acc", nativeQuery = true)
    FHIRModel getFHIRByAccesion(@Param("acc") String acc);

    @Query(value = "SELECT * FROM xray_fhir WHERE SERVICE_ID = :imaging", nativeQuery = true)
    FHIRModel getFHIRByImaging(@Param("imaging") String imaging);

    @Query(value="SELECT * FROM xray_fhir WHERE ACCESSION_NUMBER = :acc AND STATUS = :status", nativeQuery = true)
    FHIRModel getFHIRByAccesionStatus(@Param("acc") String acc, @Param("status") String status);

    @Query(value="SELECT BUNDLE_ID FROM xray_fhir WHERE ACCESSION_NUMBER = :acc AND STATUS = '1'", nativeQuery = true)
    List<String> listBundleID(@Param("acc") String acc);

    @Query(value = "SELECT COUNT(id) > 0 FROM xray_fhir WHERE BUNDLE_ID = :bunlde", nativeQuery = true)
    BigInteger existsByBundle(@Param("bunlde") String bunlde);

    @Query(value = "SELECT * FROM xray_fhir WHERE BUNDLE_ID = :bundle", nativeQuery = true)
    FHIRModel getFHIRByBundle(@Param("bundle") String bundle);
}
