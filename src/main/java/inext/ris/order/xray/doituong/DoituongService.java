package inext.ris.order.xray.doituong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class DoituongService {
    @Autowired
    DoituongRepository doituongRepository;

    public BigInteger existsByTypeID(String type_id) {
        return doituongRepository.existsByTypeID(type_id);
    }

    public DoituongModel getDoituongByTypeID(String type_id) {
        return doituongRepository.getDoituongByTypeID(type_id);
    }
}
