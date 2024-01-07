package inext.ris.order.xray.hl7.msh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MSHService {
    @Autowired
    MSHRepositoy mshRepositoy;

    public Optional<MSH> findById(Long id) {
        return mshRepositoy.findById(id);
    }
}
