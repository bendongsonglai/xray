package inext.ris.order.xray.reportsigned;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignatureService {
    @Autowired
    SignatureRepository signatureRepository;

    public List<Signature> findAll() {
        return signatureRepository.findAll();
    }

    public Optional<Signature> findById(Long id) {
        return signatureRepository.findById(id);
    }

    public Signature getOne(Long id) {
        return signatureRepository.getOne(id);
    }

    public Signature save(Signature signature) {
        return signatureRepository.save(signature);
    }

    public void deleteById(Long id) {
        signatureRepository.deleteById(id);
    }
}
