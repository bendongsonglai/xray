package inext.ris.order.xray.hapi.service;

import org.hl7.fhir.r4.model.Patient;

public interface PatientRepository {
    public void GetPatient(String mabn);
    public String getPatientIdBase(String mabn);
    public Boolean checkPatientExist(String mabn);
    public Patient getPatientData(String mabn);
}
