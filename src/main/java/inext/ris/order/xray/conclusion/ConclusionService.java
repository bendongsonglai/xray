package inext.ris.order.xray.conclusion;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class ConclusionService {
	@Autowired
	ConclusionRepository describeRepository;
	
	public List<ConclusionModel> findAll() {
		return describeRepository.findAll();
	}
	
    public Optional<ConclusionModel> findById(Long id) {
    	return describeRepository.findById(id);
    }
    
    public ConclusionModel getOne(Long id) {
    	return describeRepository.getOne(id);
    }
    
    public ConclusionModel save(ConclusionModel describe) {
        return describeRepository.save(describe);
    }
       
    public void deleteById(Long id) {
    	describeRepository.deleteById(id);
    }
}
