package inext.ris.order.xray.describe;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class DescribeService {
	@Autowired
	DescribeRepository describeRepository;
	
	public List<DescribeModel> findAll() {
		return describeRepository.findAll();
	}
	
    public Optional<DescribeModel> findById(Long id) {
    	return describeRepository.findById(id);
    }
    
    public DescribeModel getOne(Long id) {
    	return describeRepository.getOne(id);
    }
    
    public DescribeModel save(DescribeModel describe) {
        return describeRepository.save(describe);
    }
       
    public void deleteById(Long id) {
    	describeRepository.deleteById(id);
    }
}
