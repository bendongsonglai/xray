package inext.ris.order.xray.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class IPService {
    @Autowired
    IPRepository ipRepository;

    public BigInteger existsByIP(String ip) {
        return ipRepository.existsByIP(ip);
    }

    public IP getPortalByIp(String ip) {
        return ipRepository.getPortalByIp(ip);
    }
}
