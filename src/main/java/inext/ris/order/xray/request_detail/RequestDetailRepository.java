package inext.ris.order.xray.request_detail;

import java.math.BigInteger;
import java.util.Date;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestDetailRepository extends JpaRepository<RequestDetailModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_request_detail WHERE ACCESSION = :accNo", nativeQuery = true)
    public BigInteger existsByAccNo(@Param("accNo") String accNo);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_request_detail WHERE ACCESSION = :accNo AND FLAG2 IS NOT NULL", nativeQuery = true)
    public BigInteger existsByAccNoAndImage(@Param("accNo") String accNo);
    
    @Query(value="SELECT * FROM xray_request_detail WHERE ACCESSION = :accNo", nativeQuery = true)
    RequestDetailModel getRequestDetailByAccession(@Param("accNo") String accNo);
    
    @Modifying
    @Transactional
    @Query(value="UPDATE xray_request_detail set STATUS = :status, PAGE = :page, TECH1 = :tech1, ASSIGN_BY = :assign_by, START_TIME = :start_time, ASSIGN_TIME = :assign_time WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateRequestDetailByAccession(@Param("status") String status, @Param("page") String page, @Param("tech1") String tech1, @Param("assign_by") String assign_by, @Param("start_time") String start_time, @Param("assign_time") String assign_time, @Param("accNo") String accNo);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_request_detail set STATUS = :status, PAGE = :page, ASSIGN = :assign, APPROVED_TIME = :approved_time, TEMP_REPORT = :temp_report WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateToReportByAccession(@Param("status") String status, @Param("page") String page, @Param("assign") String assign, @Param("approved_time") String approved_time, @Param("temp_report") String temp_report, @Param("accNo") String accNo);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_request_detail set WORKSTATION = :workstation WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateWorkstation(@Param("workstation") String workstation, @Param("accNo") String accNo);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_request_detail set FLAG2 = :flag2, FLAG3 = :flag3, ARRIVAL_TIME = :arrival_time WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateStudy(@Param("flag2") String flag2, @Param("flag3") String flag3, @Param("arrival_time") Date arrival_time, @Param("accNo") String accNo);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_request_detail set FLAG1 = :flag1 WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateOrderControl(@Param("flag1") String flag1, @Param("accNo") String accNo);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_request_detail set FLAG3 = :flag3 WHERE ACCESSION = :accNo", nativeQuery = true)
    void updateOrderControlNormal(@Param("flag3") String flag3, @Param("accNo") String accNo);
}
