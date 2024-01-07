package inext.ris.order.xray.report.title;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TitleRepository extends JpaRepository<Title, Long> {
    @Query(value="SELECT * FROM xray_report_title WHERE XRAY_CODE = :code", nativeQuery = true)
    Title FindTitleReportByCode(@Param("code") String code);
}
