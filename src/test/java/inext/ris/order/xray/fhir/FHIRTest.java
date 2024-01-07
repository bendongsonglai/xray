package inext.ris.order.xray.fhir;

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
public class FHIRTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private FHIRRepository fhirRepository;
    @Test
    public void should_find_fhir_repository_is_empty() {
        Iterable<FHIRModel> requestDetails = fhirRepository.findAll();
        assertThat(requestDetails).isEmpty();
    }

    @Test
    public void should_store_a_fhir_order() {
        FHIRModel newFHIR = fhirRepository.save(new FHIRModel((long) 0, "123", "2311271452",
                "2311271452", null, new Date(), new Date(), new Date(), "0"));
        Optional<FHIRModel> op = fhirRepository.findById(newFHIR.getId());
        assertThat(op.isPresent()).isTrue();
    }

    @Test
    public void should_store_a_fhir_report() {
        FHIRModel newFHIR = fhirRepository.save(new FHIRModel((long) 0, "123", "2311271452",
                "2311271452", null, new Date(), new Date(), new Date(), "1"));
        Optional<FHIRModel> op = fhirRepository.findById(newFHIR.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
