package inext.ris.order.xray.signature;

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
public class SignatureAccountTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private SignatureAccountRepository signatureAccountRepository;
    @Test
    public void should_find_signature_account_repository_is_empty() {
        Iterable<SignatureAccount> signatureAccounts = signatureAccountRepository.findAll();
        assertThat(signatureAccounts).isEmpty();
    }

    @Test
    public void should_store_a_signature_account() {
        SignatureAccount newSignatureAccount= signatureAccountRepository.save(new SignatureAccount((long) 0, 3, "doctor", "apipacs",
                "api@easysign#2022", "5401100050b54270209fd98934ed7c03", "077179002935", null, null, new Date()));
        Optional<SignatureAccount> op = signatureAccountRepository.findById(newSignatureAccount.getId());
        assertThat(op.isPresent()).isTrue();
    }
}
