package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.his.model.patient.*;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PatientConvert {
    private static IGenericClient client;
    public Patient PatientFHIR(PatientData patientData) {
        Patient patient = new Patient();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Boolean existPatient;
        IdentifierPatient identifierPatient = new IdentifierPatient();
        identifierPatient.setPI(patientData.getPatientID());
        BundlePatient bundlePatient = null;
        try {
            bundlePatient = checkPatientByIdentifier(identifierPatient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Patient");
        if (bundlePatient==null) {
            existPatient = false;
        } else {
            existPatient = bundlePatient.getCheck();
        }
        //System.out.println("PatientCheck: "+existPatient);
        if (existPatient) {
            String temp = bundlePatient.getPatient().getId();
            String uuidPatient = temp.substring(temp.indexOf("Patient/")+8, temp.indexOf("/_history/"));
            log.info("Exist Patient with uuid {}", uuidPatient);
           return PatientFHIRUpdate(patientData, uuidPatient);
        } else {
            String id = UUID.randomUUID().toString();
            patient.setId(IdType.URN_PREFIX + "uuid:" + id);
            patient.setIdBase(id);
            patient.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-patient"));
            patient.setActive(true);

            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "PI", "Patient internal identifier", "http://moh.gov.vn/fhir/core/sid/patient-internal-id", patientData.getPatientID()));
            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "NI", "National unique individual identifier", "http://moh.gov.vn/fhir/core/sid/citizen_id", patientData.getNationalID()));
            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "PPN", "Passport number", "http://moh.gov.vn/fhir/core/sid/passport_number", patientData.getPassPort()));

            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "SB",
                    "Social Beneficiary Identifier", "http://moh.gov.vn/fhir/core/sid/patient-internal-id",
                    patientData.getSocialBeneficiaryNumber()).setPeriod((FHIRtype.period(patientData.getSocialBeneficiaryPeriod().getStart(), patientData.getSocialBeneficiaryPeriod().getEnd()))));

            for (int i = 0; i < patientData.getExtensionList().size(); i++) {
                String system = patientData.getExtensionList().get(i).getSystem();
                String code = patientData.getExtensionList().get(i).getCode();
                String text = patientData.getExtensionList().get(i).getText();
                if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-ethic")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-ethic", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-race")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-race", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-occupation")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-occupation", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-nationality")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-nationality", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-education-level")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-education-level", system, code, text));
                }
            }

            patient.addName(FHIRtype.patientName(patientData.getPatientName()));
            patient.addAddress(addressPatient(patientData));


            if (patientData.getGender().equals("male")) {
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            } else if (patientData.getGender().equals("female")) {
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            } else if (patientData.getGender() != null) {
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            } else {
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }


            if (patientData.getBirthDate() != null) {
                try {
                    Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(patientData.getBirthDate());

                    log.info("birthDate {}", birthDate);
                    patient.setBirthDateElement((DateType) new DateType().setValue(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                patient.setBirthDate(null);
            }
            BooleanType Dece = new BooleanType();
            patient.setDeceased(Dece.setValue(false));
            patient.addTelecom(telecomPatient(patientData.getTelecom().get(0)));
            if (patientData.getContact() != null) {
                patient.addContact(contact(patientData.getContact()));
            }
            patient.addCommunication().setLanguage(FHIRtype.code("http://hl7.org/fhir/ValueSet/all-languages ", "vi", "Việt Nam"));
            patient.setManagingOrganization(new Reference("Organization/" + patientData.getOrganizationUUID()));
            String responseID = client.update().resource(patient).execute().getId().getValue();
            log.info("Completed Patient mabn {} uuid {}", patientData.getPatientID(), responseID);
            Patient patientRes = getPatientByUUID(responseID);
            return patientRes;
        }
    }

    private  Patient patientFHIRNoCheck(PatientData patientData) {
        Patient patient = new Patient();
        FHIRtype FHIRtype = new FHIRtype();
            String id = UUID.randomUUID().toString();
            patient.setId(IdType.URN_PREFIX + "uuid:" + id);
            patient.setIdBase(id);
            patient.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-patient"));
            patient.setActive(true);

            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "PI", "Patient internal identifier", "http://moh.gov.vn/fhir/core/sid/patient-internal-id", patientData.getPatientID()));
            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "NI", "National unique individual identifier", "http://moh.gov.vn/fhir/core/sid/citizen_id", patientData.getNationalID()));
            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "PPN", "Passport number", "http://moh.gov.vn/fhir/core/sid/passport_number", patientData.getPassPort()));

            patient.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "SB",
                    "Social Beneficiary Identifier", "http://moh.gov.vn/fhir/core/sid/patient-internal-id",
                    patientData.getSocialBeneficiaryNumber()).setPeriod((FHIRtype.period(patientData.getSocialBeneficiaryPeriod().getStart(), patientData.getSocialBeneficiaryPeriod().getEnd()))));

            for (int i = 0; i < patientData.getExtensionList().size(); i++) {
                String system = patientData.getExtensionList().get(i).getSystem();
                String code = patientData.getExtensionList().get(i).getCode();
                String text = patientData.getExtensionList().get(i).getText();
                if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-ethic")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-ethic", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-race")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-race", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-occupation")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-occupation", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-nationality")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-nationality", system, code, text));
                } else if (system.equals("http://moh.gov.vn/fhir/CodeSystem/vn-core-education-level")) {
                    patient.addExtension(FHIRtype.extensionPatient("http://moh.gov.vn/fhir/core/StructureDefinition/extension-education-level", system, code, text));
                }
            }

            patient.addName(FHIRtype.patientName(patientData.getPatientName()));
            patient.addAddress(addressPatient(patientData));


            if (patientData.getGender().equals("male")) {
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            } else if (patientData.getGender().equals("female")) {
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            } else if (patientData.getGender() != null) {
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            } else {
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }


            if (patientData.getBirthDate() != null) {
                try {
                    Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(patientData.getBirthDate());

                    log.info("birthDate {}", birthDate);
                    patient.setBirthDateElement((DateType) new DateType().setValue(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                patient.setBirthDate(null);
            }
            BooleanType Dece = new BooleanType();
            patient.setDeceased(Dece.setValue(false));
            patient.addTelecom(telecomPatient(patientData.getTelecom().get(0)));
            patient.addContact(contact(patientData.getContact()));
            patient.addCommunication().setLanguage(FHIRtype.code("http://hl7.org/fhir/ValueSet/all-languages ", "vi", "Việt Nam"));
            //patient.setManagingOrganization(new Reference("Organization/" + patientData.getOrganizationUUID()));
            log.info("Completed Patient No Check mabn {}", patientData.getPatientID());
            return patient;
    }

    public Patient PatientFHIRUpdate(PatientData patientData, String uuid) {
        Patient patient = new Patient();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        try {
            patient = client.read().resource(Patient.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Patient By uuid {} error!", uuid);
        }
        Patient patientNew = patientFHIRNoCheck(patientData);
        patientNew.setId(patient.getId());
        patientNew.setIdBase(patient.getIdBase());
        String responseID = client.update().resource(patientNew).execute().getId().getValue();
        log.info("Update Patient with uuid {} completed!", uuid);
        Patient patientRes = getPatientByUUID(responseID);
        return patientRes;
    }

    private static Address addressPatient(PatientData patientData) {
        Address addressPatient = new Address();
        String id1 = UUID.randomUUID().toString();
        addressPatient.setId(id1);
        addressPatient.setText(patientData.getAddress().getText());
        addressPatient.addLine(patientData.getAddress().getLine().get(0));
        addressPatient.setCity(patientData.getAddress().getCity());
        addressPatient.setDistrict(patientData.getAddress().getDistrict());
        addressPatient.setCountry("Việt Nam");
        return addressPatient;
    }

    private static ContactPoint telecomPatient(ContactPointPatient contactPointLocal) {
        ContactPoint telecom = new ContactPoint();
        telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
        telecom.setValue(contactPointLocal.getValue());
        if (contactPointLocal.getUse().equals("HOME")) {
            telecom.setUse(ContactPoint.ContactPointUse.HOME);
        } else if (contactPointLocal.getUse().equals("WORK")) {
            telecom.setUse(ContactPoint.ContactPointUse.WORK);
        } else {
            telecom.setUse(ContactPoint.ContactPointUse.NULL);
        }
        return telecom;
    }

    private static Patient.ContactComponent contact (ContactPatient contactPatient) {
        Patient.ContactComponent contact = new Patient.ContactComponent();
        try {
            CodeableConcept relationship = new CodeableConcept();
            Coding coding = new Coding();
            coding.setSystem(contactPatient.getRelationship().getSystem());
            coding.setCode(contactPatient.getRelationship().getCode());
            coding.setDisplay(contactPatient.getRelationship().getValue());
            List<Coding> codingList = new ArrayList<>();
            codingList.add(coding);
            relationship.setCoding(codingList);
            relationship.setText(contactPatient.getRelationship().getText());
            List<CodeableConcept> codeableConcepts = new ArrayList<>();
            codeableConcepts.add(relationship);

            HumanName humanName = new HumanName();
            humanName.setText(contactPatient.getName());

            ContactPoint contactPoint = new ContactPoint();
            contactPoint.setSystem(ContactPoint.ContactPointSystem.PHONE);
            contactPoint.setValue(contactPatient.getTelecom().getValue());
            if (contactPatient.getTelecom().getUse().equals("HOME")) {
                contactPoint.setUse(ContactPoint.ContactPointUse.HOME);
            } else if (contactPatient.getTelecom().getUse().equals("WORK")) {
                contactPoint.setUse(ContactPoint.ContactPointUse.WORK);
            } else {
                contactPoint.setUse(ContactPoint.ContactPointUse.NULL);
            }
            List<ContactPoint> contactPoints = new ArrayList<>();
            contactPoints.add(contactPoint);

            contact.setRelationship(codeableConcepts);
            contact.setName(humanName);
            contact.setTelecom(contactPoints);
            if (contactPatient.getGender().equals("male")) {
                contact.setGender(Enumerations.AdministrativeGender.MALE);
            } else if (contactPatient.getGender().equals("female")) {
                contact.setGender(Enumerations.AdministrativeGender.FEMALE);
            } else if (contactPatient.getGender() != null) {
                contact.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            } else {
                contact.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }

            Address addressPatient = new Address();
            String id1 = UUID.randomUUID().toString();
            addressPatient.setId(id1);
            addressPatient.setText(contactPatient.getAddress());
            addressPatient.setCountry("Việt Nam");

            contact.setAddress(addressPatient);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Contact Patient error!");
        }
        return contact;
    }

    public Patient getPatientByUUID (String uuid) {
        Patient patient = null;
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        try {
            patient = client.read().resource(Patient.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Patient By uuid {} error!", uuid);
        }
        return patient;
    }

    public BundlePatient checkPatientByIdentifier (IdentifierPatient identifierPatient) {
        BundlePatient bundlePatient = new BundlePatient();
        bundlePatient.setCheck(false);
        bundlePatient.setPatient(null);
        try {
            Patient patient = getPatientByIdentifier(identifierPatient.getPI());
            if (!patient.equals("NULL")) {
                bundlePatient.setCheck(true);
                bundlePatient.setPatient(patient);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Check Patient by Identifier error!");
            return null;
        }
        return bundlePatient;
    }
    public Patient getPatientByIdentifier (String identifier) {
        Patient patient = null;
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        try {
            Bundle bundle = client
                    .search()
                    .forResource(Patient.class)
                    .where(Patient.IDENTIFIER.exactly().identifier(identifier))
                    .returnBundle(org.hl7.fhir.r4.model.Bundle.class)
                    .execute();
            if ( bundle.getEntry().size()==0) {
                patient = null;
            } else {
                Bundle.BundleEntryComponent patientComponent = bundle.getEntry().get(0);
                patient = (Patient) patientComponent.getResource();
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Patient By identifier {} error!", identifier);
        }
        return patient;
    }
}
