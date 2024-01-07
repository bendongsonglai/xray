package inext.ris.order.xray.fhir;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/api/v1/fhir")
@Slf4j
@RequiredArgsConstructor
public class FHIRAPI {
	@Autowired
	FHIRService fhirService;
	
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody FHIRModel fhir) {
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
    public ResponseEntity<FHIRModel> findById(@PathVariable Long id) {
        Optional<FHIRModel> fhir = fhirService.findById(id);
        if (!fhir.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fhir.get());
    }
    
    @GetMapping("/accession/{accession}")
    public ResponseEntity<FHIRModel> findByAccession(@PathVariable String accession) {
    	FHIRModel fhir = fhirService.getFHIRByAccesion(accession);
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByACC(accession).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + accession + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fhir);
    }
    
    @GetMapping("/imaging/detail/{id}")
    public ResponseEntity<FHIRModel> findByImaging(@PathVariable String id) {
    	FHIRModel fhir = fhirService.getFHIRByImaging(id);
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByImaging(id).compareTo(bi);
        if (res <= 0) {
            log.error("Imaging UUID " + id + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fhir);
    }
    
    @GetMapping("/imaging/{id}")
    public Boolean checkImaging(@PathVariable String id) {
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = fhirService.existsByImaging(id).compareTo(bi);
        if (res <= 0) {
            log.error("Imaging UUID " + id + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/status/{accession}/{status}")
    public ResponseEntity<FHIRModel> findByAccessionStatus(@PathVariable String accession, @PathVariable String status) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = fhirService.existsByACC(accession).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + accession + " is not existed");
            return ResponseEntity.noContent().build();
        }
        FHIRModel fhir = fhirService.getFHIRByAccesionStatus(accession, status);
        return ResponseEntity.ok(fhir);
    }


    @GetMapping("/listreport/{accession}")
    public ResponseEntity<List<String>> listReport(@PathVariable String accession) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = fhirService.existsByACC(accession).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + accession + " is not existed");
            return ResponseEntity.noContent().build();
        }
        List<String> bundles = fhirService.listBundleID(accession);
        return ResponseEntity.ok(bundles);
    }
    @GetMapping("/check/{acc}")
    public Boolean checkBundle(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = fhirService.existsReportByACC(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Report with acc " + acc + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/bundle/detail/{id}")
    public ResponseEntity<FHIRModel> findByBundle(@PathVariable String id) {
        FHIRModel fhir = fhirService.getFHIRByBundle(id);
        BigInteger bi = new BigInteger("0");
        int res;
        res = fhirService.existsByBundle(id).compareTo(bi);
        if (res <= 0) {
            log.error("BUNDLE_ID " + id + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fhir);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listLastOrder() {
        List<String> listOrders = new ArrayList<>();
        try {
            listOrders = fhirService.listLastOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(listOrders);
    }

    @GetMapping("/bundle/{id}")
    public Boolean checkBundleReport(@PathVariable String id) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = fhirService.existsByBundleReport(id).compareTo(bi);
        if (res <= 0) {
            log.error("BUNDLE_ID " + id + " is not existed");
            return false;
        }
        return true;
    }
    
}
