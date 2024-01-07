package inext.ris.order.xray.referrer;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class ReferrerService {
	@Autowired
	ReferrerRepository referrerRepository;
	
	public List<ReferrerModel> findAll() {
		return referrerRepository.findAll();
	}
	
    public Optional<ReferrerModel> findById(Long id) {
    	return referrerRepository.findById(id);
    }
    
    public BigInteger existsByPrac(String prac) {
    	return referrerRepository.existsByPrac(prac);
    }
    
    
    public BigInteger existsByPrefix(String prefix) {
    	return referrerRepository.existsByPrefix(prefix);
    }
    
    public ReferrerModel getReferrerByPrac(String prac) {
    	return referrerRepository.getReferrerByPrac(prac);
    }

    public ReferrerModel getReferrerByRefID(String ref) {
        return referrerRepository.getReferrerByRefID(ref);
    }
    
    public ReferrerModel getReferrerByPrefix(String prefix) {
    	return referrerRepository.getReferrerByPrefix(prefix);
    }
    
    public ReferrerModel getOne(Long id) {
    	return referrerRepository.getOne(id);
    }
    
    public ReferrerModel save(ReferrerModel referrer) {
        return referrerRepository.save(referrer);
    }
       
    public void deleteById(Long id) {
    	referrerRepository.deleteById(id);
    }
}
