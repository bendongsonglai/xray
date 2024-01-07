package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.ReferenceParam;
import inext.ris.order.xray.his.model.ConditionData;
import inext.ris.order.xray.his.model.encounter.EncounterData;
import inext.ris.order.xray.his.model.encounter.EncounterDischargeData;
import inext.ris.order.xray.his.model.encounter.EncounterTransferData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter.DiagnosisComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class EncounterConvert {
    private static IGenericClient client;

    public Encounter EncounterAdmitFHIR(EncounterData encounterData) {
        Encounter encounter = new Encounter();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        encounter.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-encounter"));
        encounter.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number", "http://moh.gov.vn/fhir/core/sid/MRN", encounterData.getMedicalRecordNumber()));
        encounter.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "VN", "Visit number", "http://moh.gov.vn/fhir/core/sid/VisitNumber", encounterData.getAdmitNumber()));
        encounter.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "U", "Unspecified identifier", null, encounterData.getManagementNumber()));
        String id = UUID.randomUUID().toString();
        encounter.setId(IdType.URN_PREFIX + "uuid:" + id);
        encounter.setIdBase(id);

        encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
        if (encounterData.getClassOrigin().equals("IMP")) {
            encounter.setClass_(
                    FHIRtype.coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
        } else if (encounterData.getClassOrigin().equals("EMER")) {
            encounter.setClass_(
                    FHIRtype.coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "EMER", "emergency"));
        } else {
            encounter.setClass_(
                    FHIRtype.coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory encounter"));
        }

        encounter.setSubject(new Reference("Patient/" + patientComponent(encounterData.getSubject())));
        if (encounterData.getDatetime() != null) {
            encounter.setPeriod(FHIRtype.period(encounterData.getAdmitDatetime()));
        }


        String location = getUUIDLocationByCode(encounterData.getLocationWardCode());
        EncounterLocationComponent locationCom = new EncounterLocationComponent();
        locationCom.setLocation(new Reference("Location/" + location));
        locationCom.setPhysicalType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/location-physical-type", "wa", "Ward"));
        locationCom.setStatus(Encounter.EncounterLocationStatus.ACTIVE);
        locationCom.setPeriod(FHIRtype.period(encounterData.getDatetime()));

        encounter.addLocation(locationCom);

        if (encounterData.getLocationBedCode() != null) {
            String locationGiuong = locationComponent(encounterData.getLocationBedCode());
            EncounterLocationComponent locationComGiuong = new EncounterLocationComponent();
            locationComGiuong.setLocation(new Reference("Location/" + locationGiuong));

            locationComGiuong.setStatus(Encounter.EncounterLocationStatus.ACTIVE);
            locationComGiuong.setPhysicalType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/location-physical-type", "bd", "Bed"));
            locationComGiuong.setPeriod(FHIRtype.period(encounterData.getDatetime()));
            encounter.addLocation(locationComGiuong);
        }
        if (!encounterData.getLocationTransferCode().equals("NULL") || !encounterData.getLocationTransferCode().equals("")) {
            encounter.setHospitalization(hospitalizationComponent(encounterData));
        }
        for (int i = 0; i < encounterData.getEncounterPractitionerList().size(); i++) {
            encounter.addParticipant()
                    .addType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/v3-ParticipationType", encounterData.getEncounterPractitionerList().get(i).getCode()
                            , encounterData.getEncounterPractitionerList().get(i).getText()))
                    .setIndividual(new Reference("Practitioner/"
                            + practitionerService(encounterData.getEncounterPractitionerList().get(i).getPractitioner())));
        }

        String Organization = getUUIDOrganizationByCode(encounterData.getOrganizationCode());
        encounter.setServiceProvider(new Reference("Organization/" + Organization));
        String responseID = client.update().resource(encounter).execute().getId().getValue();
        try {
            EncounterUpdateConditionFHIR(encounterData, encounter, id);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Update condition error!");
        }
        log.info("Completed Encounter Admit maql {} UUID {}", encounterData.getManagementNumber(), encounter.getIdBase());
        return getEncounterByUUID(responseID);
    }

    public void EncounterUpdateConditionFHIR(EncounterData encounterData, Encounter encounter, String id) {
        if (encounterData.getEncounterDiagnosisList() != null) {
            for (int i = 0; i < encounterData.getEncounterDiagnosisList().size(); i++) {
                if (!encounterData.getEncounterDiagnosisList().get(i).getType().equals("") || !encounterData.getEncounterDiagnosisList().get(i).getType().equals("NULL")) {
                    ConditionData conditionData = new ConditionData();
                    conditionData.setSubject(encounterData.getSubject());
                    conditionData.setEncounter(id);
                    conditionData.setIcd(encounterData.getEncounterDiagnosisList().get(i).getIcd());
                    conditionData.setDiagnosis(encounterData.getEncounterDiagnosisList().get(i).getDiagnosis());
                    conditionData.setAdmitDatetime(encounterData.getAdmitDatetime());
                    if (encounterData.getEncounterDiagnosisList().get(i).getType().equals("AD")) {
                        conditionData.setPractitioner(encounterData.getEncounterPractitionerList().get(0).getPractitioner());
                    } else {
                        conditionData.setPractitioner(encounterData.getEncounterPractitionerList().get(1).getPractitioner());
                    }
                    String conditionUUID = conditionUUID(conditionData);
                    encounter.addDiagnosis(DiagnosisComponent(conditionUUID, encounterData.getEncounterDiagnosisList().get(i).getType()));
                }
            }
        }
        client.update().resource(encounter).execute().getId().getValue();
        log.info("Update Encounter Condition complete!!");
    }

    public Encounter EncounterAdmitFHIRNotCreate(EncounterData encounterData) {
        Encounter encounter = new Encounter();
        FHIRtype FHIRtype = new FHIRtype();
        encounter.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-encounter"));
        encounter.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number", "http://moh.gov.vn/fhir/core/sid/MRN", encounterData.getMedicalRecordNumber()));
        encounter.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "VN", "Visit number", "http://moh.gov.vn/fhir/core/sid/VisitNumber", encounterData.getAdmitNumber()));
        encounter.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "U", "Unspecified identifier", null, encounterData.getManagementNumber()));
        String id = UUID.randomUUID().toString();
        encounter.setId(IdType.URN_PREFIX + "uuid:" + id);
        encounter.setIdBase(id);

        encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);
        if (encounterData.getClassOrigin().equals("IMP")) {
            encounter.setClass_(
                    FHIRtype.coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
        } else if (encounterData.getClassOrigin().equals("EMER")) {
            encounter.setClass_(
                    FHIRtype.coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "EMER", "emergency"));
        } else if (encounterData.getClassOrigin().equals("NONAC")) {
            encounter.setClass_(
                    FHIRtype.coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "NONAC", "inpatient non-acute"));
        }

        encounter.setSubject(new Reference("Patient/" + encounterData.getSubject()));
        if (encounterData.getDatetime() != null) {
            encounter.setPeriod(FHIRtype.period(encounterData.getAdmitDatetime()));
        }
        /*for (int i = 0; i < encounterData.getEncounterDiagnosisList().size(); i++) {
            if (!encounterData.getEncounterDiagnosisList().get(i).getType().equals("") || !encounterData.getEncounterDiagnosisList().get(i).getType().equals("NULL")) {
                ConditionData conditionData = new ConditionData();
                conditionData.setSubject(encounterData.getSubject());
                conditionData.setEncounter(id);
                conditionData.setIcd(encounterData.getEncounterDiagnosisList().get(i).getIcd());
                conditionData.setDiagnosis(encounterData.getEncounterDiagnosisList().get(i).getDiagnosis());
                conditionData.setAdmitDatetime(encounterData.getAdmitDatetime());
                if (encounterData.getEncounterDiagnosisList().get(i).getType().equals("AD")) {
                    conditionData.setPractitioner(encounterData.getEncounterPractitionerList().get(0).getPractitioner());
                } else {
                    conditionData.setPractitioner(encounterData.getEncounterPractitionerList().get(1).getPractitioner());
                }
                String conditionUUID = conditionUUID(conditionData);
                encounter.addDiagnosis(DiagnosisComponent(conditionUUID, encounterData.getEncounterDiagnosisList().get(i).getType()));
            }
        } su dung ham update condition*/

        String location = locationComponent(encounterData.getLocationWardCode());
        EncounterLocationComponent locationCom = new EncounterLocationComponent();
        locationCom.setLocation(new Reference("Location/" + location));
        locationCom.setPhysicalType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/location-physical-type", "wa", "Ward"));
        locationCom.setStatus(Encounter.EncounterLocationStatus.ACTIVE);
        locationCom.setPeriod(FHIRtype.period(encounterData.getDatetime()));

        encounter.addLocation(locationCom);

        if (encounterData.getLocationBedCode() != null) {
            String locationGiuong = locationComponent(encounterData.getLocationBedCode());
            EncounterLocationComponent locationComGiuong = new EncounterLocationComponent();
            locationComGiuong.setLocation(new Reference("Location/" + locationGiuong));

            locationComGiuong.setStatus(Encounter.EncounterLocationStatus.ACTIVE);
            locationComGiuong.setPhysicalType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/location-physical-type", "bd", "Bed"));
            locationComGiuong.setPeriod(FHIRtype.period(encounterData.getDatetime()));
            encounter.addLocation(locationComGiuong);
        }
        if (!encounterData.getLocationTransferCode().equals("NULL") || !encounterData.getLocationTransferCode().equals("")) {
            encounter.setHospitalization(hospitalizationComponent(encounterData));
        }
        for (int i = 0; i < encounterData.getEncounterPractitionerList().size(); i++) {
            encounter.addParticipant()
                    .addType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/v3-ParticipationType", encounterData.getEncounterPractitionerList().get(i).getCode()
                            , encounterData.getEncounterPractitionerList().get(i).getText()))
                    .setIndividual(new Reference("Practitioner/"
                            + practitionerService(encounterData.getEncounterPractitionerList().get(i).getPractitioner())));
        }

        String Organization = OrganizationComponent(encounterData.getLocationWardCode());
        encounter.setServiceProvider(new Reference("Organization/" + Organization));
        log.info("Completed Encounter Admit not create maql {} UUID {}", encounterData.getManagementNumber(), encounter.getIdBase());
        return encounter;
    }

    public Encounter EncounterDischargeFHIR(EncounterDischargeData encounterDischargeData) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Encounter encounterDischarge;
        try {
            encounterDischarge = client.read().resource(Encounter.class).withId(encounterDischargeData.getEncounter()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Update Encounter Discharge error!");
            return null;
        }
        encounterDischarge.setStatus(Encounter.EncounterStatus.FINISHED);
        Period PeriodStart = encounterDischarge.getPeriod();
        Date endDate = new Date();
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(encounterDischargeData.getDischargeDatetime());
            PeriodStart.setEnd(endDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        encounterDischarge.setPeriod(PeriodStart);
        if (encounterDischargeData.getEncounterPractitioner() != null) {
            log.info("Encounter Discharge MABS {}", encounterDischargeData.getEncounterPractitioner().getPractitioner());
            encounterDischarge.addParticipant()
                    .addType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/v3-ParticipationType", encounterDischargeData.getEncounterPractitioner().getCode()
                            , encounterDischargeData.getEncounterPractitioner().getText()))
                    .setIndividual(new Reference("Practitioner/"
                            + practitionerService(encounterDischargeData.getEncounterPractitioner().getPractitioner())));
        }
        long getDiff = endDate.getTime() - PeriodStart.getStart().getTime();
        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);
        encounterDischarge.setLength((Duration) new Duration().setValue(getDaysDiff));
        if (encounterDischargeData.getEncounterDiagnosis() != null) {
            ConditionData conditionData = new ConditionData();
            conditionData.setSubject(encounterDischargeData.getSubject());
            conditionData.setEncounter(encounterDischargeData.getEncounter());
            conditionData.setIcd(encounterDischargeData.getEncounterDiagnosis().getIcd());
            conditionData.setDiagnosis(encounterDischargeData.getEncounterDiagnosis().getDiagnosis());
            conditionData.setAdmitDatetime(encounterDischargeData.getDischargeDatetime());
            if (encounterDischargeData.getEncounterDiagnosis().getType().equals("DD")) {
                if (encounterDischargeData.getEncounterPractitioner() != null) {
                    conditionData.setPractitioner(encounterDischargeData.getEncounterPractitioner().getPractitioner());
                } else {
                    if (encounterDischarge.getParticipant().size() > 1) {
                        String practitioner_uuid = encounterDischarge.getParticipant().get(1).getIndividual().getReference().split("/")[1];
                        conditionData.setPractitioner(practitionerCode(practitioner_uuid));
                    }
                }
            }
            String conditionUUID = conditionUUID(conditionData);
            encounterDischarge.addDiagnosis(DiagnosisComponent(conditionUUID, encounterDischargeData.getEncounterDiagnosis().getType()));
        }

        Encounter.EncounterHospitalizationComponent HospitalizationComponent = encounterDischarge.getHospitalization();
        if (encounterDischargeData.getResult() != null) {
            HospitalizationComponent.setDischargeDisposition(FHIRtype.code(null, encounterDischargeData.getResult(), "Khỏi"));
        }
        client.update().resource(encounterDischarge).execute();
        log.info("Completed Encounter Discharge maql {} UUID {}", encounterDischarge.getIdentifier().get(2).getValue(), encounterDischarge.getIdBase());
        return encounterDischarge;
    }

    public Encounter EncounterTransferFHIR(EncounterTransferData encounterTransferData) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Encounter encounterTrans;
        try {
            encounterTrans = client.read().resource(Encounter.class).withId(encounterTransferData.getEncounter()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Update Fetch Encounter Transfer error!");
            return null;
        }
        encounterTrans.setStatus(Encounter.EncounterStatus.ONLEAVE);
        List<EncounterLocationComponent> listLocation = encounterTrans.getLocation();
        Date endDate = new Date();
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(encounterTransferData.getTransferDatetime());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        for (EncounterLocationComponent location : listLocation) {
            Period locationPeriod = location.getPeriod();
            locationPeriod.setEnd(endDate);
        }
        client.update().resource(encounterTrans).execute();
        log.info("Completed Encounter Transfer maql {} UUID {}", encounterTrans.getIdentifier().get(2).getValue(), encounterTrans.getIdBase());
        return encounterTrans;
    }

    public Encounter EncounterTransferPartOfFHIR(EncounterData encounterData, String encounterUUID) {
        EncounterConvert encounterConvert = new EncounterConvert();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Encounter encounterOld;
        try {
            encounterOld = client.read().resource(Encounter.class).withId(encounterUUID).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Update Fetch Encounter Transfer PartOf error!");
            return null;
        }
        Encounter encounter;
        encounter = encounterConvert.EncounterAdmitFHIRNotCreate(encounterData);
        encounter.setPeriod(FHIRtype.period(encounterData.getDatetime()));
        encounter.setPartOf(new Reference("Encounter/" + encounterOld.getIdElement().getIdPart()));
        String responseID = client.update().resource(encounter).execute().getId().getValue();
        try {
            EncounterUpdateConditionFHIR(encounterData, encounter, responseID);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Update condition error!");
        }
        log.info("Completed Encounter Transfer PartOf maql {} UUID {}", encounterData.getManagementNumber(), encounter.getIdBase());
        return getEncounterByUUID(responseID);
    }

    public List<String> findEncounterByIdentifier(String identifier) {
        EncounterConvert encounterConvert = new EncounterConvert();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        List<String> uuidList = new ArrayList<>();
        Bundle results = client.search().forResource(Encounter.class)
                .where(Encounter.IDENTIFIER.exactly().identifier(identifier)).returnBundle(Bundle.class).execute();
        log.info("Encounter List total {}", results.getTotal());
        if (results.getTotal() != 0) {
            List<BundleEntryComponent> components = results.getEntry();
            for (BundleEntryComponent component : components) {
                String id = component.getResource().getIdElement().getIdPart();
                uuidList.add(id);
            }
        } else {
            log.error("Encounter List = 0.");
        }
        return uuidList;
    }

    public List<String> findEncounterBySubject(String subject) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        List<String> uuidList = new ArrayList<>();
        IIdType patientId = client.read().resource(Patient.class).withId(subject).execute().getIdElement();
        ;
        ReferenceParam patientReference = new ReferenceParam(patientId);
        Bundle results = client.search().forResource(Encounter.class)
                .where(Encounter.SUBJECT.hasId(patientReference.getIdPart()))
                .returnBundle(Bundle.class)
                .execute();
        log.info("Encounter List total {}", results.getTotal());
        if (results.getTotal() != 0) {
            List<BundleEntryComponent> components = results.getEntry();
            for (BundleEntryComponent component : components) {
                Encounter encounter = (Encounter) component.getResource();
                //System.out.println("Status"+encounter.getStatus());
                if (!encounter.getStatus().toString().equals("FINISHED")) {
                    String id = component.getResource().getIdElement().getIdPart();
                    uuidList.add(id);
                }
            }
        } else {
            log.error("Encounter List = 0.");
        }
        return uuidList;
    }

    private static String conditionUUID(ConditionData conditionData) {
        ConditionConvert conditionConvert = new ConditionConvert();
        Condition conditon = conditionConvert.ConditionFHIR(conditionData);
        return conditon.getIdBase();
    }

    private static DiagnosisComponent DiagnosisComponent(String condition, String code) {
        DiagnosisComponent DiagnosisComponent = new DiagnosisComponent();
        FHIRtype FHIRtype = new FHIRtype();
        DiagnosisComponent.setCondition(new Reference("Condition/" + condition));
        String id = UUID.randomUUID().toString();
        DiagnosisComponent.setId(id);
        switch (code) {
            case "AD":
                DiagnosisComponent.setUse(
                        FHIRtype.code("http://terminology.hl7.org/CodeSystem/diagnosis-role", "AD", "Admission diagnosis"));
                break;
            case "DD":
                DiagnosisComponent.setUse(
                        FHIRtype.code("http://terminology.hl7.org/CodeSystem/diagnosis-role", "DD", "Discharge diagnosis"));
                break;
            case "CC":
                DiagnosisComponent.setUse(
                        FHIRtype.code("http://terminology.hl7.org/CodeSystem/diagnosis-role", "CC", "Chief complaint"));
                break;
            case "CM":
                DiagnosisComponent.setUse(
                        FHIRtype.code("http://terminology.hl7.org/CodeSystem/diagnosis-role", "CM", "Comorbidity diagnosis"));
                break;
        }
        return DiagnosisComponent;
    }

    private static String locationComponent(String makp) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Bundle results = client.search()
                .forResource(Location.class)
                .where(Location.IDENTIFIER.exactly()
                        .identifier(makp))
                .returnBundle(Bundle.class).execute();
        log.info("Encounter Location List total {}", results.getTotal());
        String id = null;
        List<BundleEntryComponent> components = results.getEntry();
        for (BundleEntryComponent component : components) {
            Location location = (Location) component.getResource();
            id = location.getIdElement().getIdPart();
        }
        return id;
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
    private static Encounter.EncounterHospitalizationComponent hospitalizationComponent(EncounterData EncounterData) {
        Encounter.EncounterHospitalizationComponent hospitalizationComponent = new Encounter.EncounterHospitalizationComponent();
        String location = locationComponent(EncounterData.getLocationTransferCode());
        hospitalizationComponent.setOrigin(new Reference("Location/" + location));
        return hospitalizationComponent;
    }

    private static String OrganizationComponent(String makp) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Bundle results = client.search().forResource(Organization.class)
                .where(Organization.IDENTIFIER.exactly().code(makp))
                .returnBundle(Bundle.class).execute();
        log.info("Encounter Organization List total {}", results.getTotal());
        String id = null;
        List<BundleEntryComponent> components = results.getEntry();
        for (BundleEntryComponent component : components) {
            id = component.getResource().getIdElement().getIdPart();
        }
        return id;
    }

    private static String practitionerService(String mabs) {
        Bundle results = client.search().forResource(Practitioner.class)
                .where(Practitioner.IDENTIFIER.exactly().identifier(mabs)).returnBundle(Bundle.class).execute();
        log.info("Encounter Practitioner List total {}", results.getTotal());
        String id = null;
        if (results.getTotal() != 0) {
            List<BundleEntryComponent> components = results.getEntry();
            for (BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        } else {
            log.error("Encounter Practitioner List = 0.");
            Bundle results1 = client.search().forResource(Practitioner.class)
                    .where(Practitioner.IDENTIFIER.exactly().identifier("1")).returnBundle(Bundle.class).execute();
            List<Bundle.BundleEntryComponent> components = results1.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }

    private String practitionerCode(String uuid) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Practitioner practitioner;
        String codebs = null;
        try {
            practitioner = client.read().resource(Practitioner.class).withId(uuid).execute();
            codebs = practitioner.getIdentifier().get(0).getValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Find Practitioner error!");
        }
        return codebs;
    }

    public Encounter getEncounterByUUID(String uuid) {
        Encounter encounter = null;
        try {
            FHIRtype FHIRtype = new FHIRtype();
            client = FHIRtype.connectFHIR();
            encounter = client.read().resource(Encounter.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Encounter By uuid {} error!", uuid);
        }
        return encounter;
    }
    private static String getUUIDLocationByCode (String code) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Location.class)
                .where(Location.IDENTIFIER.exactly().identifier(code)).returnBundle(Bundle.class).execute();
        log.info("Get Location code {}", code);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }
    private static String getUUIDOrganizationByCode (String code) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Organization.class)
                .where(Organization.IDENTIFIER.exactly().identifier(code)).returnBundle(Bundle.class).execute();
        log.info("Get Organization MAKP {}", code);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }
    public static void main(String[] args) {
        System.out.println(locationComponent("001"));
    }
}
