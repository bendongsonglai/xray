package inext.ris.order.xray.referrer;

import inext.ris.order.xray.BKRISJpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BKRISJpaConfig.class})
@ActiveProfiles("dev")
public class ReferrerTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private ReferrerRepository referrerRepository;
    @Test
    public void should_find_referrer_repository_is_empty() {
        Iterable<ReferrerModel> referrers = referrerRepository.findAll();
        assertThat(referrers).isEmpty();
    }

    @Test
    public void should_store_a_referrer() {
        ReferrerModel newReferrer = referrerRepository.save(new ReferrerModel((long) 0, "001", "MD", "NGUYỄN VĂN ",
                "TEST", "NGUYEN VAN ", "TEST", null, "M", "BKRIS", "0000/HCM-CCHN"));
        Optional<ReferrerModel> op = referrerRepository.findById(newReferrer.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
