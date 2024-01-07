package inext.ris.order.xray.aes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AESService {
    @Autowired
    AESRepository aesRepository;

    public AES getAes() {
        return aesRepository.getAes();
    }
}
