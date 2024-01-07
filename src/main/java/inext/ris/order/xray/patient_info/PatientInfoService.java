package inext.ris.order.xray.patient_info;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class PatientInfoService {
	@Autowired
	PatientInfoRepository patientInfoRespository;
	public List<PatientInfoModel> findAll() {
		return patientInfoRespository.findAll();
	}
	
    public Optional<PatientInfoModel> findById(Long id) {
    	return patientInfoRespository.findById(id);
    }
    
    public PatientInfoModel getOne(Long id) {
    	return patientInfoRespository.getOne(id);
    }
    
    public BigInteger existsByMRN(String mrn) {
    	return patientInfoRespository.existsByMRN(mrn);
    }
    
    public PatientInfoModel getPatientByMrn (String mrn) {
    	return patientInfoRespository.getPatientByMrn(mrn);
    }
    
    public void updatePatientByMrn(String ssn, String mrn) {
    	patientInfoRespository.updatePatientByMrn(ssn, mrn);
    }

    public void updatePatientInfoByMrn(String ssn, String mrn, String sex, Date dob, String tele, String name,
                                       String lastname, String addr, String xn, String name_old, String lastname_old) {
        patientInfoRespository.updatePatientInfoByMrn(ssn, mrn, sex, dob, tele, name,
                lastname, addr, xn, name_old, lastname_old);
    }
    
    public PatientInfoModel save(PatientInfoModel patient) {
        return patientInfoRespository.save(patient);
    }
       
    public void deleteById(Long id) {
    	patientInfoRespository.deleteById(id);
    }
}
