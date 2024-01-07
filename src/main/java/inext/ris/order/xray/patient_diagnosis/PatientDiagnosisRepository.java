package inext.ris.order.xray.patient_diagnosis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PatientDiagnosisRepository extends JpaRepository<PatientDiagnosis, Long> {
    @Query(value="SELECT a.MRN as mrn, a.ACCESSION as acc, b.NAME as ce_name, a.CE_VAL as ce_val, \n" +
            "b.UNIT as unit, c.NAME as mp_name, c.VALUE as mp_val ,a.DATE as datetime \n" +
            "FROM xray_patient_diagnosis a\n" +
            "INNER JOIN xray_clinical_element b ON a.CE = b.ID \n" +
            "INNER JOIN xray_major_problem c ON a.MP = c.ID \n" +
            "WHERE a.MRN = :mrn AND a.ENABLED = 0", nativeQuery = true)
    List<Map<String, Object>> patientDiagnosissEnabledByMRN(@Param("mrn") String mrn);

    @Query(value="SELECT DATE_FORMAT(FROM_DAYS(DATEDIFF(now(), d.BIRTH_DATE)), '%Y') + 0 AS age, d.SEX as sex, b.NAME as ce_name, a.CE_VAL as ce_val,  \n" +
            "b.UNIT as unit, c.NAME as mp_name, c.VALUE as mp_val ,a.DATE as datetime \n" +
            "FROM xray_patient_diagnosis a\n" +
            "INNER JOIN xray_clinical_element b ON a.CE = b.ID \n" +
            "INNER JOIN xray_major_problem c ON a.MP = c.ID \n" +
            "INNER JOIN xray_patient_info d ON a.MRN = d.MRN \n" +
            "WHERE a.ACCESSION = :acc AND a.ENABLED = 0", nativeQuery = true)
    List<Map<String, Object>> patientDiagnosissEnabledByACC(@Param("acc") String acc);

    @Query(value="SELECT * FROM xray_patient_diagnosis WHERE CE = :index AND CE_VAL = 'ABNORMAL' AND ENABLED = 0 ORDER BY ID DESC limit 5", nativeQuery = true)
    List<PatientDiagnosis> listByCe(@Param("index") Integer index);
}
