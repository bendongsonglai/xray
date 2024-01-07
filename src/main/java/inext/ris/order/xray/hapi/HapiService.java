package inext.ris.order.xray.hapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HapiService {
    @Autowired
    HapiRepository hapiRepository;

    public Hapi getHapi() {
        return hapiRepository.getHapi();
    }
}
