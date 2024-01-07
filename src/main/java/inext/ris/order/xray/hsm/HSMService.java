package inext.ris.order.xray.hsm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HSMService {
    @Autowired
    HSMRepository hsmRepository;

    public List<HSMModel> findAll() {
        return hsmRepository.findAll();
    }

    public Optional<HSMModel> findById(Long id) {
        return hsmRepository.findById(id);
    }

    public HSMModel getOne(Long id) {
        return hsmRepository.getOne(id);
    }

    public HSMModel save(HSMModel hsm) {
        return hsmRepository.save(hsm);
    }

    public void deleteById(Long id) {
        hsmRepository.deleteById(id);
    }
}
