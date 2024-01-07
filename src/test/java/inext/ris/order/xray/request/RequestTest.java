package inext.ris.order.xray.request;

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
public class RequestTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;
    @Test
    public void should_find_request_repository_is_empty() {
        Iterable<RequestModel> requests = requestRepository.findAll();
        assertThat(requests).isEmpty();
    }

    @Test
    public void should_store_a_request() {
        RequestModel newCode = requestRepository.save(new RequestModel((long) 0, "2311281356", "123456", "123456",
                "1", new Date(), new Date(), "001", null, "doctor", 1, null, null, null, "1",
                null, "ABDOMINAL US", "BKRIS"));
        Optional<RequestModel> op = requestRepository.findById(newCode.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
