package inext.ris.order.xray.user;

import inext.ris.order.xray.BKRISJpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BKRISJpaConfig.class})
@ActiveProfiles("dev")
public class UserTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    @Test
    public void should_find_user_repository_is_empty() {
        Iterable<UserModel> users = userRepository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    public void should_store_a_user() {
        String hashPassword = "";
        try {
            hashPassword = md5(md5("added_salt"), md5("Abc123"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        UserModel newFHIR = userRepository.save(new UserModel((long) 0, "doctor", "doctor",
                "bacsi", "Bác sĩ ", "iNext", "Bac si ", "iNext", "RADIOLOGIST", null,
                hashPassword, "BKRIS", new Date(), "", "1", 0, new Date(), "", null));
        Optional<UserModel> op = userRepository.findById(newFHIR.getId());
        assertThat(op.isPresent()).isTrue();
    }

    public static String md5(String plainText) throws NoSuchAlgorithmException {
        return md5(null, plainText);
    }

    public static String md5(String salt, String plainText)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        if (salt != null) {
            md.update(salt.getBytes());
        }
        md.update(plainText.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }
}
