package inext.ris.order.xray.hislog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HisLogService {
    @Autowired
    HisLogRepository hisLogRepository;

    public Optional<HisLog> findById(Long id) {
        return hisLogRepository.findById(id);
    }

    public HisLog getOne(Long id) {
        return hisLogRepository.getOne(id);
    }

    public HisLog save(HisLog fhir) {
        return hisLogRepository.save(fhir);
    }
}
