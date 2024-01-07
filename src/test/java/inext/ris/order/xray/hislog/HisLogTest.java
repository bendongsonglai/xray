package inext.ris.order.xray.hislog;

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
public class HisLogTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private HisLogRepository hisLogRepository;
    @Test
    public void should_find_hislog_repository_is_empty() {
        Iterable<HisLog> departments = hisLogRepository.findAll();
        assertThat(departments).isEmpty();
    }

    @Test
    public void should_store_a_hislog_sync_report() {
        HisLog newHisLog = hisLogRepository.save(new HisLog((long) 0, "486", "2311281606", "200",
                "Cập nhật trạng thái thành công", "OK", new Date()));
        Optional<HisLog> op = hisLogRepository.findById(newHisLog.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
