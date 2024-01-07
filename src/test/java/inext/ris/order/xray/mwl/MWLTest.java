package inext.ris.order.xray.mwl;

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
public class MWLTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private MWLRepository mwlRepository;
    @Test
    public void should_find_hl7v2_repository_is_empty() {
        Iterable<MWLModel> mwls = mwlRepository.findAll();
        assertThat(mwls).isEmpty();
    }

    @Test
    public void should_store_a_hl7v2_order() {
        MWLModel newFHIR = mwlRepository.save(new MWLModel((long) 0, "2311281542", "123456",
                new Date(),  new Date(), "0"));
        Optional<MWLModel> op = mwlRepository.findById(newFHIR.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
