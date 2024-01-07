package inext.ris.order.xray.patient_diagnosis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PatientDiagnosisService {
    @Autowired
    PatientDiagnosisRepository patientDiagnosisRepository;

    public List<PatientDiagnosis> findAll() {
        return patientDiagnosisRepository.findAll();
    }

    public Optional<PatientDiagnosis> findById(Long id) {
        return patientDiagnosisRepository.findById(id);
    }

    public PatientDiagnosis getOne(Long id) {
        return patientDiagnosisRepository.getOne(id);
    }


    public List<Map<String, Object>> patientDiagnosissEnabledByMRN(String mrn) {
        return patientDiagnosisRepository.patientDiagnosissEnabledByMRN(mrn);
    }


    public List<Map<String, Object>> patientDiagnosissEnabledByACC(String acc) {
        return patientDiagnosisRepository.patientDiagnosissEnabledByACC(acc);
    }


    public List<PatientDiagnosis> listByCe(Integer index) {
        return patientDiagnosisRepository.listByCe(index);
    }

    public PatientDiagnosis save(PatientDiagnosis patientDiagnosis) {
        return patientDiagnosisRepository.save(patientDiagnosis);
    }

    public void deleteById(Long id) {
        patientDiagnosisRepository.deleteById(id);
    }
}
