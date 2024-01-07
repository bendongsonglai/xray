package inext.ris.order.xray.code;
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
public class CodeTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private CodeRepository codeRepository;
    @Test
    public void should_find_code_repository_is_empty() {
        Iterable<CodeModel> codes = codeRepository.findAll();
        assertThat(codes).isEmpty();
    }

    @Test
    public void should_store_a_code() {
        CodeModel newCode = codeRepository.save(new CodeModel((long) 0, "BKRIS", "DX001", "Chụp Xquang ngực thẳng",
                "CR", "CHEST", (float) 100, 0, 0, 0, "0", "0",0));
        Optional<CodeModel> op = codeRepository.findById(newCode.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
