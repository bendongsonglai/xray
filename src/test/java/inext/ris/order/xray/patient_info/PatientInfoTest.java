package inext.ris.order.xray.patient_info;

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
public class PatientInfoTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Test
    public void should_find_patient_repository_is_empty() {
        Iterable<PatientInfoModel> patients = patientInfoRepository.findAll();
        assertThat(patients).isEmpty();
    }

    @Test
    public void should_store_a_patient() {
        PatientInfoModel newPatient = patientInfoRepository.save(new PatientInfoModel((long) 0, "BKRIS", "123456", "123456",
                "unknow", null, "NGUYỄN VĂN TEST", "", "NGUYEN VAN TEST", "", "", "", "M", new Date(), "0367209442",
                "", "", new Date(), null, null, "", "393 Lạc Long Quân, P5, Quận 11, HCM", null, "",
                "", "","",""));
        Optional<PatientInfoModel> op = patientInfoRepository.findById(newPatient.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
