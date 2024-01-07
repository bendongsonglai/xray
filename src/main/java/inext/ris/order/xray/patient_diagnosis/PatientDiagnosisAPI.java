package inext.ris.order.xray.patient_diagnosis;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patient_diagnosis")
@Slf4j
@RequiredArgsConstructor
public class PatientDiagnosisAPI {
    @Autowired
    PatientDiagnosisService patientDiagnosisService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody PatientDiagnosis patientDiagnosis) {
        return ResponseEntity.ok(patientDiagnosisService.save(patientDiagnosis));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDiagnosis> findById(@PathVariable Long id) {
        Optional<PatientDiagnosis> patientDiagnosis = patientDiagnosisService.findById(id);
        if (!patientDiagnosis.isPresent()) {
            log.error("PatientDiagnosis Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(patientDiagnosis.get());
    }

    @GetMapping("/list/{index}")
    public ResponseEntity<?> listCE(@PathVariable String index) {
        try {
            List<PatientDiagnosis> patientDiagnoses = patientDiagnosisService.listByCe(Integer.parseInt(index));
            return new ResponseEntity<>(patientDiagnoses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mrn/{mrn}")
    public ResponseEntity<?> patientDiagnosisByMrn(@PathVariable String mrn) {
        List<Map<String, Object>> patientDiagnoses = patientDiagnosisService.patientDiagnosissEnabledByMRN(mrn);
        return new ResponseEntity<>(patientDiagnoses, HttpStatus.OK);
    }

    @GetMapping("/acc/{acc}")
    public ResponseEntity<?> findByAccession(@PathVariable String acc) {
        List<Map<String, Object>> patientDiagnoses = patientDiagnosisService.patientDiagnosissEnabledByACC(acc);
        return new ResponseEntity<>(patientDiagnoses, HttpStatus.OK);
    }


}
