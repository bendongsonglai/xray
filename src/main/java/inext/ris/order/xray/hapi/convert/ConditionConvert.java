package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.his.model.ConditionData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ConditionConvert {
    private static IGenericClient client;

    public Condition ConditionFHIR(ConditionData conditionData) {
        Condition condition = new Condition();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        String id = UUID.randomUUID().toString();
        condition.setId(IdType.URN_PREFIX + "uuid:" + id);
        condition.setIdBase(id);
        condition.setSubject(new Reference("Patient/" + patientComponent(conditionData.getSubject())));
        condition.setEncounter(new Reference("Encounter/" + conditionData.getEncounter()));
        condition.setClinicalStatus(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-ver-status", "resolved", "resolved"));
        condition.setVerificationStatus(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "confirmed"));
        condition.addCategory(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-category", "encounter-diagnosis", "encounter-diagnosis"));
        condition.setCode(FHIRtype.code("http://terminology.hl7.org/CodeSystem/icd10", conditionData.getIcd(), conditionData.getDiagnosis()));
        Date tDate;
        try {
            tDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(conditionData.getAdmitDatetime());
            condition.setRecordedDateElement(new DateTimeType(tDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        condition.setRecorder(new Reference("Practitioner/" + practitionerService(conditionData.getPractitioner())));
        client.update().resource(condition).execute();
        log.info("Compledted Condition subject {} !", conditionData.getSubject());
        return condition;
    }

    public Condition addConditionEncounterFHIR(Condition condition, String encounter) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        condition.setEncounter(new Reference("Encounter/" + encounter));
        String responseID = client.update().resource(condition).execute().getId().getValue();
        log.info("Completed Condition add by encounter {}", encounter);
        return getConditionByUUID(responseID);
    }


    public Condition ConditionCdkemtheoFHIR(String subject, String encounter, String Maicd, String Chandoan, String ngayud) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Condition condition = new Condition();
        String id = UUID.randomUUID().toString();
        condition.setId(IdType.URN_PREFIX + "uuid:" + id);
        condition.setIdBase(id);

        condition.setSubject(new Reference("Patient/" + patientComponent(subject)));
        condition.setEncounter(new Reference("Encounter/" + encounter));

        condition.setClinicalStatus(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-ver-status", "resolved", "resolved"));
        condition.setVerificationStatus(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "confirmed"));
        condition.addCategory(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-category", "encounter-diagnosis", "encounter-diagnosis"));
        condition.setCode(FHIRtype.code("http://terminology.hl7.org/CodeSystem/icd10", Maicd, Chandoan));
        Date tDate;
        try {
            tDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ngayud);
            condition.setRecordedDateElement(new DateTimeType(tDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String responseID = client.update().resource(condition).execute().getId().getValue();
        log.info("Completed Condition comorbidity diagnostic by subject {} encounter {} maicd {} chandoan {} ngayud {}", subject, encounter, Maicd, Chandoan, ngayud);
        return getConditionByUUID(responseID);
    }


    public String ConditionStringFHIR(String subject, String encounter, String Maicd, String Chandoan, String Practitioner) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Condition condition = new Condition();
        String id = UUID.randomUUID().toString();
        condition.setId(IdType.URN_PREFIX + "uuid:" + id);
        condition.setIdBase(id);

        condition.setSubject(new Reference("Patient/" + patientComponent(subject)));
        condition.setEncounter(new Reference("Encounter/" + encounter));

        condition.setClinicalStatus(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-ver-status", "resolved", "resolved"));
        condition.setVerificationStatus(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "confirmed"));
        condition.addCategory(FHIRtype.code("http://terminology.hl7.org/CodeSystem/condition-category", "encounter-diagnosis", "encounter-diagnosis"));
        condition.setCode(FHIRtype.code("http://terminology.hl7.org/CodeSystem/icd10", Maicd, Chandoan));


        condition.setRecorder(new Reference("Practitioner/" + Practitioner));
        String responseID = client.update().resource(condition).execute().getId().getValue();
        log.info("Created Condition url {}", responseID);
        String idBase = getConditionByUUID(responseID).getIdBase();
        return idBase.substring(idBase.indexOf("Condition/") + 10);
    }


    private static String practitionerService(String mabs) {

        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Practitioner.class)
                .where(Practitioner.IDENTIFIER.exactly().identifier(mabs)).returnBundle(Bundle.class).execute();
        log.info("Get Pactitioner mabs {}", mabs);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        } else {
            Bundle results1 = client.search().forResource(Practitioner.class)
                    .where(Practitioner.IDENTIFIER.exactly().identifier("1")).returnBundle(Bundle.class).execute();
            List<Bundle.BundleEntryComponent> components = results1.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }

    public Condition getConditionByUUID(String uuid) {
        Condition condition = null;
        try {
            FHIRtype FHIRtype = new FHIRtype();
            client = FHIRtype.connectFHIR();
            condition = client.read().resource(Condition.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Condition By uuid {} error!", uuid);
        }
        return condition;
    }

    private static String patientComponent(String pid) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().identifier(pid)).returnBundle(Bundle.class).execute();
        log.info("Get Patient PID {}", pid);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }
}
