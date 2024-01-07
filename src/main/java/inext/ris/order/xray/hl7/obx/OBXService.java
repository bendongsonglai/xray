package inext.ris.order.xray.hl7.obx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OBXService {
    @Autowired
    OBXRepository obxRepository;

    public Optional<OBX> findById(Long id) {
        return obxRepository.findById(id);
    }
}
