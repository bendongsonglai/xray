package inext.ris.order.xray.hapi.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.hapi.convert.FHIRtype;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

@Slf4j
public class PatientService implements PatientRepository{
    @Override
    public void GetPatient(String mabn) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Bundle results =  null;
        try {
            results = client.search()
                    .forResource(Patient.class)
                    .where(Patient.IDENTIFIER.exactly().identifier(mabn))
                    .returnBundle(Bundle.class).execute();
            log.info("Fetch Patient with mabn {} completed!", mabn);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get Patient mabn {} error!", mabn);
        }
    }

    @Override
    public String getPatientIdBase(String mabn) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Bundle results =  null;
        String id = null;
        try {
            results = client.search()
                    .forResource(Patient.class)
                    .where(Patient.IDENTIFIER.exactly().identifier(mabn))
                    .returnBundle(Bundle.class).execute();
            log.info("Fetch Patient with mabn {} completed!", mabn);
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            id = components.get(0).getResource().getIdElement().getIdPart();
            log.info("patientID {}",id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get Patient ID Base with mabn {} error!", mabn);
        }
        return id;
    }

    @Override
    public Boolean checkPatientExist(String mabn) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Bundle results =  null;
        try {
            results = client.search()
                    .forResource(Patient.class)
                    .where(Patient.IDENTIFIER.exactly().identifier(mabn))
                    .returnBundle(Bundle.class).execute();
            log.info("Fetch Patient with mabn {} completed!", mabn);
            if (results.getTotal() >= 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Check Patient Exist with mabn {} error!", mabn);
        }
        return false;
    }

    @Override
    public Patient getPatientData(String mabn) {
        Patient patient = null;
        Bundle results =  null;
        try {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            patient = (Patient) components.get(0).getResource();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get Patient Data with mabn {} error!",mabn);
        }
        return patient;
    }
}
