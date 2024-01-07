package inext.ris.order.xray.delete;


import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {
	@Autowired
	DeleteRepository deleteRepository;
	
    public BigInteger existsByBundleID(String bundle_id) {
    	return deleteRepository.existsByBundleID(bundle_id);
    }

    public DeleteModel save(DeleteModel delete) {
        return deleteRepository.save(delete);
    }

}
