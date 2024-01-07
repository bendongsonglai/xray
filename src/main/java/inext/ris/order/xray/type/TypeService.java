package inext.ris.order.xray.type;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeService {
	@Autowired
	TypeRepository typeRepository;
	
	public List<TypeModel> findAll() {
		return typeRepository.findAll();
	}
	
    public Optional<TypeModel> findById(Long id) {
    	return typeRepository.findById(id);
    }
    
    public TypeModel getOne(Long id) {
    	return typeRepository.getOne(id);
    }
    
    public TypeModel FindXrayType(String type) {
    	return typeRepository.FindXrayType(type);
    }
    
    public TypeModel save(TypeModel patient) {
        return typeRepository.save(patient);
    }
       
    public void deleteById(Long id) {
    	typeRepository.deleteById(id);
    }
}
