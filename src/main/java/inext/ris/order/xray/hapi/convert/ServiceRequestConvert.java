package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.his.model.ConceptCode;
import inext.ris.order.xray.his.model.serviceRequest.IdentifierServiceRequest;
import inext.ris.order.xray.his.model.serviceRequest.ServiceRequestData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ServiceRequestConvert {
    private static IGenericClient client;

    public ServiceRequest ServiceRequest(ServiceRequestData serviceRequestData) {
        ServiceRequest serviceRequest = new ServiceRequest();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        String id = UUID.randomUUID().toString();
        serviceRequest.setId(IdType.URN_PREFIX + "uuid:" + id);
        serviceRequest.setIdBase(id);
        serviceRequest.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-service-request"));

        for (int i = 0; i < serviceRequestData.getIdentifierServiceRequests().size(); i++) {
            serviceRequest.addIdentifier(identifier(serviceRequestData.getIdentifierServiceRequests().get(i)));
        }

        serviceRequest.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
        serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
        serviceRequest.setCategory(listCategory(serviceRequestData.getCategory()));
        serviceRequest.addCategory(code(serviceRequestData.getCategory().get(1)));

        serviceRequest.setRequisition(identifier(serviceRequestData.getRequisition()));
        serviceRequest.setCode(code(serviceRequestData.getCode()));
        String patientUID = patientComponent(serviceRequestData.getSubject());
        String encounterUID = encounterComponent(serviceRequestData.getEncounter());
        serviceRequest.setSubject(new Reference("Patient/" + patientUID));
        serviceRequest.setEncounter(new Reference("Encounter/" + encounterUID));

        Date ngaychidinh, ngaythuchien;
        try {
            ngaychidinh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(serviceRequestData.getAuthoredOn());
            serviceRequest.setAuthoredOnElement(new DateTimeType(ngaychidinh));
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("Parse Datetime AutoredOn error!");
        }

        try {
            ngaythuchien = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(serviceRequestData.getOccurrenceDateTime());
            serviceRequest.setOccurrence(new DateTimeType(ngaythuchien));
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("Parse Datetime Occurrence error!");
        }

        if (serviceRequestData.getPerformer() != null) {
            serviceRequest.addPerformer(new Reference("Practitioner/" + practitionerService(serviceRequestData.getPerformer())));
        }
        //log.info("Mabs: {}", serviceRequestData.getRequester());
        serviceRequest.setRequester(new Reference("Practitioner/" + practitionerService(serviceRequestData.getRequester())));

        ConditionConvert ConditionConvertor = new ConditionConvert();
        String idReason = ConditionConvertor.ConditionStringFHIR(serviceRequestData.getSubject(), encounterUID, serviceRequestData.getReason().getCode(), serviceRequestData.getReason().getText(), serviceRequestData.getRequester());
        serviceRequest.addReasonReference(new Reference("Condition/" + idReason));

        serviceRequest.setQuantity(new Quantity(Integer.valueOf(serviceRequestData.getQuantity())));
        String responseID = client.update().resource(serviceRequest).execute().getId().getValue();
        log.info("Creating ServiceRequest uuid {} id {}", serviceRequest.getIdBase(), responseID);
        return getServiceRequestByUUID(responseID);
    }

    private Identifier identifier(IdentifierServiceRequest identifierServiceRequest) {
        Identifier identifier = new Identifier();
        if (identifierServiceRequest.getUse().equals("OFFICIAL")) {
            identifier.setUse(Identifier.IdentifierUse.OFFICIAL);
        } else if (identifierServiceRequest.getUse().equals("SECONDARY")) {
            identifier.setUse(Identifier.IdentifierUse.SECONDARY);
        } else {
            identifier.setUse(Identifier.IdentifierUse.TEMP);
        }
        CodeableConcept type = new CodeableConcept();
        List<Coding> codingList = new ArrayList<>();
        Coding coding = new Coding();
        coding.setSystem(identifierServiceRequest.getType().getSystem());
        coding.setDisplay(identifierServiceRequest.getType().getValue());
        coding.setCode(identifierServiceRequest.getType().getCode());
        codingList.add(coding);
        type.setCoding(codingList);
        type.setText(identifierServiceRequest.getType().getText());
        identifier.setType(type);
        identifier.setSystem(identifierServiceRequest.getSystem());
        identifier.setValue(identifierServiceRequest.getValue());
        return identifier;
    }

    public static DateTimeType DateTime(String DateTime) {
        DateTimeType DateTimeType = new DateTimeType();
        Date DateTimeDone = new Date();
        if (DateTime != null) {
            try {
                DateTimeDone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DateTime);
                DateTimeType.setValue(DateTimeDone);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return DateTimeType;
    }

    private List<CodeableConcept> listCategory(List<ConceptCode> category) {
        List<CodeableConcept> listCategory = new ArrayList<CodeableConcept>();
        try {
            CodeableConcept code = new CodeableConcept();
            List<Coding> codingList = new ArrayList<>();
            Coding coding = new Coding();
            coding.setSystem(category.get(0).getSystem());
            coding.setCode(category.get(0).getCode());
            coding.setDisplay(category.get(0).getValue());
            codingList.add(coding);
            code.setCoding(codingList);
            code.setText(category.get(0).getText());
            listCategory.add(code);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create ListCategory error!");
        }
        return listCategory;
    }

    private CodeableConcept code(ConceptCode code) {
        CodeableConcept codeResult = new CodeableConcept();
        try {
            List<Coding> codingList = new ArrayList<>();
            Coding coding = new Coding();
            coding.setSystem(code.getSystem());
            coding.setCode(code.getCode());
            coding.setDisplay(code.getValue());
            codingList.add(coding);
            codeResult.setCoding(codingList);
            codeResult.setText(code.getText());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create Code error!");
        }
        return codeResult;
    }

    private static String practitionerService(String mabs) {
        Bundle results = client.search().forResource(Practitioner.class)
                .where(Practitioner.IDENTIFIER.exactly().identifier(mabs)).returnBundle(Bundle.class).execute();
        log.info("Encounter Practitioner List total {}", results.getTotal());
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        } else {
            log.error("ServiceRequest Practitioner List = 0.");
            Bundle results1 = client.search().forResource(Practitioner.class)
                    .where(Practitioner.IDENTIFIER.exactly().identifier("1")).returnBundle(Bundle.class).execute();
            List<Bundle.BundleEntryComponent> components = results1.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
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

    private static String encounterComponent(String acc) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Encounter.class)
                .where(Encounter.IDENTIFIER.exactly().identifier(acc)).returnBundle(Bundle.class).execute();
        log.info("Get Encounter ACC {}", acc);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }

    public ServiceRequest getServiceRequestByUUID(String uuid) {
        ServiceRequest serviceRequest = null;
        try {
            FHIRtype FHIRtype = new FHIRtype();
            client = FHIRtype.connectFHIR();
            serviceRequest = client.read().resource(ServiceRequest.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch ServiceRequest By uuid {} error!", uuid);
        }
        return serviceRequest;
    }
}
