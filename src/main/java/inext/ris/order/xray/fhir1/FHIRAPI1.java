package inext.ris.order.xray.fhir1;

import java.math.BigInteger;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/fhiremr")
@Slf4j
@RequiredArgsConstructor
public class FHIRAPI1 {
	@Autowired
    FHIRService1 fhirService;
	
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody FHIRModel1 fhir) {
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByACC(fhir.getAccession_number()).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION_NUMBER " + fhir.getAccession_number() + " is existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fhirService.save(fhir));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FHIRModel1> findById(@PathVariable Long id) {
        Optional<FHIRModel1> fhir = fhirService.findById(id);
        if (!fhir.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(fhir.get());
    }
    
    @GetMapping("/accession/{accession}")
    public ResponseEntity<FHIRModel1> findByAccession(@PathVariable String accession) {
    	FHIRModel1 fhir = fhirService.getFHIRByAccesion(accession);
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByACC(accession).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + accession + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fhir);
    }
    
    @GetMapping("/bundle/detail/{id}")
    public ResponseEntity<FHIRModel1> findByBundle(@PathVariable String id) {
    	FHIRModel1 fhir = fhirService.getFHIRByBundle(id);
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByBundle(id).compareTo(bi);
        if (res <= 0) {
            log.error("BUNDLE_ID " + id + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fhir);
    }
    
    @GetMapping("/bundle/{id}")
    public Boolean checkBundle(@PathVariable String id) {
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByBundle(id).compareTo(bi);
        if (res <= 0) {
            log.error("BUNDLE_ID " + id + " is not existed");
            return false;
        }
        return true;
    }
    
}
