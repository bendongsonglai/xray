package inext.ris.order.xray.report;

import inext.ris.order.xray.BKRISJpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BKRISJpaConfig.class})
@ActiveProfiles("dev")
public class ReportTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private ReportRepository reportRepository;
    @Test
    public void should_find_report_repository_is_empty() {
        Iterable<ReportModel> reports = reportRepository.findAll();
        assertThat(reports).isEmpty();
    }

    @Test
    public void should_store_a_report() {
        ReportModel newReport = reportRepository.save(new ReportModel((long) 0, "2311271452", "Lý do khảo sát – Indicated reason: Kham Tong Quat \n Mô tả - DESCRIBE:", "KẾT LUẬN - IMPRESSION: \n",
                "Đề nghị mang theo báo cáo này cho lần khám sau", "Kết Quả Siêu Âm Vùng Chậu ", null, null, null, "", null, null, null, null,
                "2", new Date(), new Date(),
        "2", new Date(), new Date(), null));
        Optional<ReportModel> op = reportRepository.findById(newReport.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
