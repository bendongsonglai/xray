package inext.ris.order.xray.patient_info;
import java.math.BigInteger;
import java.util.Date;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientInfoRepository extends JpaRepository<PatientInfoModel, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_patient_info WHERE MRN = :mrn", nativeQuery = true)
    BigInteger existsByMRN(@Param("mrn") String mrn);
    
    @Query(value="SELECT * FROM xray_patient_info WHERE MRN = :mrn", nativeQuery = true)
    PatientInfoModel getPatientByMrn(@Param("mrn") String mrn);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_patient_info set SSN = :ssn, XN = :xn, xray_patient_info.NAME = :name, " +
            "LASTNAME = :lastname, SEX = :sex, BIRTH_DATE = :dob, TELEPHONE = :tele, ADDRESS = :addr, NAME_OLD = :name_old, " +
            "LASTNAME_OLD = :lastname_old WHERE MRN = :mrn", nativeQuery = true)
    void updatePatientInfoByMrn(@Param("ssn") String ssn, @Param("mrn") String mrn, @Param("sex") String sex,
                                            @Param("dob") Date dob, @Param("tele") String tele, @Param("name") String name,
                                            @Param("lastname") String lastname, @Param("addr") String addr, @Param("xn") String xn,
                                            @Param("name_old") String name_old, @Param("lastname_old") String lastname_old);

    @Modifying
    @Transactional
    @Query(value="UPDATE xray_patient_info set SSN = :ssn WHERE MRN = :mrn", nativeQuery = true)
    PatientInfoModel updatePatientByMrn(@Param("ssn") String ssn, @Param("mrn") String mrn);
}
