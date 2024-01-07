package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.his.model.PractitionerData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PractitionerConvert {
    private static IGenericClient client;
    public Practitioner PractitionerFHIR (PractitionerData practitionerData) {
        Practitioner practitioner = new Practitioner();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        String id = UUID.randomUUID().toString();
        practitioner.setId(IdType.URN_PREFIX + "uuid:"+id);
        practitioner.setIdBase(id);
        practitioner.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-practitioner"));
        practitioner.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "EI", "Employee number"
                , "http://moh.gov.vn/fhir/core/sid/healthworker-employee-number"
                ,practitionerData.getMabs()));

        practitioner.setActive(true);

        List<Practitioner.PractitionerQualificationComponent> practitionerQualificationComponent = new ArrayList<>();
        practitionerQualificationComponent.add(new Practitioner.PractitionerQualificationComponent().addIdentifier(
                FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203",
                        "LN",
                        "License number" ,
                        "http://moh.gov.vn/fhir/core/sid/healthworker-license-number",
                        practitionerData.getCertification())));
        Reference referenceOrganization = new Reference();
        referenceOrganization.setReference("Organization/"+getUUIDOrganizationByCode(practitionerData.getOrganization()));
        practitionerQualificationComponent.add(new Practitioner.PractitionerQualificationComponent().setIssuer(referenceOrganization));
        practitioner.setQualification(practitionerQualificationComponent);
        practitioner.addName(FHIRtype.patientName(practitionerData.getName()));
        practitioner.addAddress().setText(practitionerData.getAddress()).setCountry("VN");

        if (practitionerData.getGender().equals("M")) {
            practitioner.setGender(Enumerations.AdministrativeGender.MALE);
        } else if (practitionerData.getGender().equals("F")) {
            practitioner.setGender(Enumerations.AdministrativeGender.FEMALE);
        } else if (practitionerData.getGender()!= null) {
            practitioner.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        } else {
            practitioner.setGender(Enumerations.AdministrativeGender.OTHER);
        }

        List<ContactPoint> listTelecom = new ArrayList<ContactPoint>();
        ContactPoint telecomPhone = new ContactPoint();
        telecomPhone.setSystem(ContactPoint.ContactPointSystem.PHONE);
        telecomPhone.setValue(practitionerData.getPhone());
        listTelecom.add(telecomPhone);
        practitioner.setTelecom(listTelecom);
        String responseID = client.update().resource(practitioner).execute().getId().getValue();
        log.info("Completed Practitioner ma {} uuid {}", practitionerData.getMabs(), responseID);
        return getPractitionerByUUID(responseID);
    }


    public String practitionerService(String mabs) {
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

    public Practitioner getPractitionerByUUID(String uuid) {
        Practitioner practitioner = null;
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        try {
            practitioner = client.read().resource(Practitioner.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Practitioner By uuid {} error!", uuid);
        }
        return practitioner;
    }

    public  Boolean checkPractitionerByMabs (String mabs) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Practitioner.class)
                .where(Practitioner.IDENTIFIER.exactly().identifier(mabs)).returnBundle(Bundle.class).execute();
        String id = null;
        //System.out.println("total:"+results.getTotal());
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
            if (id.length() > 0) {
                return true;
            }
        }
        return false;
    }
}
