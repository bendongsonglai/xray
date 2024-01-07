package inext.ris.order.xray.workstation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class WSService {
    @Autowired
    WSRepository wsRepository;

    public BigInteger existsByTypeID(Integer ID) {
        return wsRepository.existsByTypeID(ID);
    }

    public BigInteger existsByAe(String ae) {
        return wsRepository.existsByAe(ae);
    }

    public WSModel getWorkstationByTypeID(Integer ID) {
        return wsRepository.getWorkstationByTypeID(ID);
    }

    public WSModel getWorkstationByAe(String ae) {
        return wsRepository.getWorkstationByAe(ae);
    }
}
