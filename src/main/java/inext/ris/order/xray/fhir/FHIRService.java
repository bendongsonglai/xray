package inext.ris.order.xray.fhir;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class FHIRService {
	@Autowired
	FHIRRepository fhirRepository;
	
    public Optional<FHIRModel> findById(Long id) {
    	return fhirRepository.findById(id);
    }
    
    public FHIRModel getOne(Long id) {
    	return fhirRepository.getOne(id);
    }
    
    public BigInteger existsByImaging(String imaging) {
    	return fhirRepository.existsByImaging(imaging);
    }
    
    public BigInteger existsByACC(String acc) {
    	return fhirRepository.existsByACC(acc);
    }

    public BigInteger existsReportByACC(String acc) {
        return fhirRepository.existsReportByACC(acc);
    }

    public BigInteger existsByBundle(String bundle_id) {
        return fhirRepository.existsByBundle(bundle_id);
    }

    public BigInteger existsByBundleReport(String bundle_id) {
        return fhirRepository.existsReportByBundleReport(bundle_id);
    }
    
    public FHIRModel getFHIRByAccesion(String acc) {
    	return fhirRepository.getFHIRByAccesion(acc);
    }

    public FHIRModel getFHIRByBundle(String bundle_id) {
        return fhirRepository.getFHIRByBundle(bundle_id);
    }

    public List<String> listLastOrder() {
        return fhirRepository.listLastOrder();
    }

    public List<String> listBundleID(String acc) {
        return fhirRepository.listBundleID(acc);
    }
    public FHIRModel getFHIRByAccesionStatus(String acc, String status) {
        return fhirRepository.getFHIRByAccesionStatus(acc, status);
    }
    
    public FHIRModel getFHIRByImaging(String imaging) {
    	return fhirRepository.getFHIRByImaging(imaging);
    }
    
    public FHIRModel save(FHIRModel fhir) {
        return fhirRepository.save(fhir);
    }
       
    public void deleteById(Long id) {
    	fhirRepository.deleteById(id);
    }
}
