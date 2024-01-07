package inext.ris.order.xray.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends JpaRepository<RequestModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_request WHERE REQUEST_NO = :acc", nativeQuery = true)
    BigInteger existsByAccNo(@Param("acc") String acc);
    @Query(value="SELECT COUNT(id) > 0 FROM xray_request WHERE MRN = :mrn AND REQUEST_NO = :acc", nativeQuery = true)
    BigInteger existsByAccNoAndMrn(@Param("mrn") String mrn, @Param("acc") String acc);

    @Query(value="SELECT * FROM xray_request WHERE REQUEST_NO = :acc", nativeQuery = true)
    RequestModel getRequestByAcc(@Param("acc") String acc);

    @Query(value="select CONCAT (a.REQUEST_NO, \"|\", c.CENTER ) \n" +
            "from xray_request a\n" +
            "inner join xray_referrer b on a.REFERRER = b.REFERRER_ID\n" +
            "inner join xray_department c on b.CENTER_CODE = c.CENTER\n" +
            "where a.REQUEST_DATE between :from and :to", nativeQuery = true)
    List<String> listStudy(@Param("from") Date from, @Param("to") Date  to);
}
