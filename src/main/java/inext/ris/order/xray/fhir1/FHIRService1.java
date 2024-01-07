package inext.ris.order.xray.fhir1;

import java.math.BigInteger;
import java.util.Optional;

import inext.ris.order.xray.fhir1.FHIRModel1;
import inext.ris.order.xray.fhir1.FHIRRepository1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class FHIRService1 {
	@Autowired
    FHIRRepository1 fhirRepository;
	
    public Optional<FHIRModel1> findById(Long id) {
    	return fhirRepository.findById(id);
    }
    
    public FHIRModel1 getOne(Long id) {
    	return fhirRepository.getOne(id);
    }
    
    public BigInteger existsByBundle(String bunlde) {
    	return fhirRepository.existsByBundle(bunlde);
    }
    
    public BigInteger existsByACC(String acc) {
    	return fhirRepository.existsByACC(acc);
    }
    
    public FHIRModel1 getFHIRByAccesion(String acc) {
    	return fhirRepository.getFHIRByAccesion(acc);
    }
    
    public FHIRModel1 getFHIRByBundle(String bundle) {
    	return fhirRepository.getFHIRByBundle(bundle);
    }
    
    public FHIRModel1 save(FHIRModel1 fhir) {
        return fhirRepository.save(fhir);
    }
       
    public void deleteById(Long id) {
    	fhirRepository.deleteById(id);
    }
}
