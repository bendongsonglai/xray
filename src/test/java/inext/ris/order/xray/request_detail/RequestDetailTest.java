package inext.ris.order.xray.request_detail;

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
public class RequestDetailTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private RequestDetailRepository requestDetailRepository;
    @Test
    public void should_find_request_detail_repository_is_empty() {
        Iterable<RequestDetailModel> requestDetails = requestDetailRepository.findAll();
        assertThat(requestDetails).isEmpty();
    }

    @Test
    public void should_store_a_request_detail() {
        RequestDetailModel newReport = requestDetailRepository.save(new RequestDetailModel((long) 0, "2311271452", "2311271452", "CT100",
                null, null, null, null, new Date(), new Date(), new Date(), null, null, null, null, "0", "0", null, new Date(), new Date(), new Date(),
                null, new Date(), new Date(), "doctor", "3", "FINAL", "END", "", "0", null, null,  new Date(), null, null, null,
                null, null, null, null));
        Optional<RequestDetailModel> op = requestDetailRepository.findById(newReport.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
