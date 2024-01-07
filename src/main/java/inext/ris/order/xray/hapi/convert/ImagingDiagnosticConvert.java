package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.hapi.service.ImagingDiagnosticService;
import inext.ris.order.xray.his.model.ImagingData;
import inext.ris.order.xray.his.model.ResultsImgData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ImagingDiagnosticConvert {
    private static IGenericClient client;
    public String ImagingDiagnosticConvertor (ResultsImgData resultsImgData, String subject, String encounter) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        ImagingDiagnosticService imagingDiagnosticService = new ImagingDiagnosticService();
        String procedureImagId ;
        String DiagnosticReportImagId ;
        if(imagingDiagnosticService.checkProcedureIdentifier(resultsImgData.getIdchidinh())) {
            return null;
        } else {
            DiagnosticReport	DiagnosticReportImag = DiagnosticReportImag(resultsImgData, subject, encounter);
            DiagnosticReportImagId = DiagnosticReportImag.getIdBase();
            log.info("Creating DiagnosticReport Img id {}",resultsImgData.getIdketqua());
            client.update().resource(DiagnosticReportImag).execute();
            Procedure procedureImag = procedureImag(resultsImgData, subject, encounter, DiagnosticReportImagId);
            procedureImagId = procedureImag.getIdBase();
            log.info("Creating Procedure Img id {}",resultsImgData.getIdketqua());
            client.update().resource(procedureImag).execute();
            return  DiagnosticReportImagId+" "+procedureImagId;
        }
    }
    public ImagingStudy ImagingConvertor (ImagingData imagingData) {
        ImagingStudy imagingStudy = new ImagingStudy();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        String id = UUID.randomUUID().toString();
        imagingStudy.setId(IdType.URN_PREFIX + "uuid:" + id);
        imagingStudy.setIdBase(id);
        Identifier identifier = new Identifier();
        identifier.setUse(Identifier.IdentifierUse.OFFICIAL);
        identifier.setSystem("HIS");
        identifier.setValue(imagingData.getAccessionNumber());
        imagingStudy.addIdentifier(identifier);
        imagingStudy.setStatus(ImagingStudy.ImagingStudyStatus.AVAILABLE);
        imagingStudy.setNumberOfSeries(1);
        List<ImagingStudy.ImagingStudySeriesComponent> series = new ArrayList<>();
        ImagingStudy.ImagingStudySeriesComponent imagingStudySeriesComponent = new ImagingStudy.ImagingStudySeriesComponent();
        Coding coding = new Coding();
        coding.setSystem("http://dicom.nema.org/resources/ontology/DCM");
        coding.setCode(imagingData.getModality());
        imagingStudySeriesComponent.setModality(coding);
        imagingStudySeriesComponent.setDescription(imagingData.getDescription());
        imagingStudySeriesComponent.setNumberOfInstances(1);
        List<ImagingStudy.ImagingStudySeriesPerformerComponent> imagingStudySeriesPerformerComponents = new ArrayList<>();
        ImagingStudy.ImagingStudySeriesPerformerComponent imagingStudySeriesPerformerComponent = new ImagingStudy.ImagingStudySeriesPerformerComponent();
        CodeableConcept function = new CodeableConcept();
        List<Coding> codingList = new ArrayList<>();
        Coding coding1 = new Coding();
        coding1.setSystem("http://terminology.hl7.org/CodeSystem/v3-ParticipationType");
        coding1.setCode("PPRF");
        codingList.add(coding1);
        function.setCoding(codingList);
        imagingStudySeriesPerformerComponent.setFunction(function);
        imagingStudySeriesPerformerComponent.setActor(new Reference("Practitioner/" + practitionerService(imagingData.getActor())));
        imagingStudySeriesPerformerComponents.add(imagingStudySeriesPerformerComponent);
        imagingStudySeriesComponent.setPerformer(imagingStudySeriesPerformerComponents);
        series.add(imagingStudySeriesComponent);
        imagingStudy.setSeries(series);
        String patientUID = patientComponent(imagingData.getSubject());
        String encounterUID= encounterComponent(imagingData.getAccessionNumber());
        String basedOnUID = serviceRequestComponent(imagingData.getAccessionNumber());
        imagingStudy.setSubject(new Reference("Patient/" + patientUID));
        imagingStudy.setEncounter(new Reference("Encounter/" + encounterUID));
        Reference reference = new Reference("ServiceRequest/" + basedOnUID);
        List<Reference> basedOn = new ArrayList<>();
        basedOn.add(reference);
        imagingStudy.setBasedOn(basedOn);
        String responseID =  client.update().resource(imagingStudy).execute().getId().getValue();
        log.info("Creating ImagingStudy uuid {} id {}", imagingStudy.getIdBase(), responseID);
        return getImagingStudyByUUID(responseID);
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

    private static String serviceRequestComponent(String acc) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(ServiceRequest.class)
                .where(ServiceRequest.IDENTIFIER.exactly().identifier(acc)).returnBundle(Bundle.class).execute();
        log.info("Get ServiceRequest ACC {}", acc);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }

    public Procedure procedureImag (ResultsImgData resultsImgData, String subject, String encounter, String DiagnosticReportImagId) {
        Procedure procedureImag = new Procedure();
        FHIRtype FHIRtype = new FHIRtype();
        String id = UUID.randomUUID().toString();
        procedureImag.setId(IdType.URN_PREFIX + "uuid:"+id);
        procedureImag.setIdBase(id);

        procedureImag.addIdentifier(identifier("maql", resultsImgData.getMaql()));
        procedureImag.addIdentifier(identifier("mavaovien", resultsImgData.getMavaovien()));
        procedureImag.addIdentifier(identifier("idchidinh", resultsImgData.getIdchidinh()));
        ServiceRequest service = getServiceRequest(resultsImgData.getIdchidinh());

        List<Identifier> list = service.getIdentifier();
        for (Identifier identifier : list) {
            if(identifier.getSystem().equals("accessionnumber")) {
                procedureImag.addIdentifier(identifier("accessionnumber",identifier.getValue()));
            }
        }
        procedureImag.setStatus(Procedure.ProcedureStatus.COMPLETED);
        procedureImag.setSubject(new Reference("Patient/"+ subject));
        procedureImag.setEncounter(new Reference("Encounter/"+encounter));
        procedureImag.setCategory(FHIRtype.code("http://snomed.info/sct", "103693007", "Diagnostic procedure"));
        procedureImag.setCode(FHIRtype.code(null, resultsImgData.getMakt(), resultsImgData.getTenkt()));
        procedureImag.addReasonCode(FHIRtype.code("http://terminology.hl7.org/CodeSystem/icd10", resultsImgData.getMaicd(), resultsImgData.getChandoan()));
        procedureImag.setOutcome(FHIRtype.code(null, null, resultsImgData.getMota()));

        procedureImag.addBasedOn(new Reference("ServiceRequest/" + service.getIdElement().getIdPart()));
        Date tDate;
        try {
            tDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultsImgData.getNgay());
            procedureImag.setPerformed(new DateTimeType(tDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String performer = practitionerService(resultsImgData.getMabs());
        procedureImag.addPerformer().setActor(new Reference("Practitioner/" + performer));

        procedureImag.addReport(new Reference("DiagnosticReport/"+ DiagnosticReportImagId));
        log.info("Completed Procedure Img!");
        return procedureImag;
    }

    public DiagnosticReport DiagnosticReportImag(ResultsImgData resultsImgData, String subject, String encounter) {
        DiagnosticReport reportImag = new DiagnosticReport();
        FHIRtype FHIRtype = new FHIRtype();

        String id = UUID.randomUUID().toString();
        reportImag.setId(IdType.URN_PREFIX + "uuid:"+id);
        reportImag.setIdBase(id);
        reportImag.addIdentifier(identifier("maql", resultsImgData.getMaql()));
        reportImag.addIdentifier(identifier("mavaovien", resultsImgData.getMavaovien()));
        reportImag.addIdentifier(identifier("idchidinh", resultsImgData.getIdchidinh()));
        reportImag.setStatus(DiagnosticReport.DiagnosticReportStatus.APPENDED);
        reportImag.setSubject(new Reference("Patient/" + subject));
        reportImag.setEncounter(new Reference("Encounter/" + encounter));
        Date tDate;
        try {
            tDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultsImgData.getNgay());
            reportImag.setEffective(new DateTimeType(tDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reportImag.setConclusion(resultsImgData.getKetluan());
        log.info("Completed DiagnosticReport Img!");
        return reportImag;
    }

    private String practitionerService(String mabs) {
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        Bundle results = client
                .search()
                .forResource(Practitioner.class)
                .where(Practitioner.IDENTIFIER.exactly().identifier(mabs))
                .returnBundle(Bundle.class)
                .execute();
        log.info("Get mabs: {}",mabs);
        String id =null;
        if(results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components=  results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id= component.getResource().getIdElement().getIdPart();
            }
        } else {
            log.error("ServiceRequest Practitioner result size = 0!");
            Bundle results1 = client.search().forResource(Practitioner.class)
                    .where(Practitioner.IDENTIFIER.exactly().identifier("1")).returnBundle(Bundle.class).execute();
            List<Bundle.BundleEntryComponent> components = results1.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }

    private static Identifier identifier (String sys, String value) {
        Identifier iden = new Identifier();
        String id1 = UUID.randomUUID().toString();
        iden.setId(id1);
        iden.setUse(Identifier.IdentifierUse.OFFICIAL);
        iden.setSystem("http://localhost/bvtn/"+sys);
        iden.setValue(value);
        return iden;
    }

    private static ServiceRequest getServiceRequest(String idchidinh) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Bundle results = client
                .search()
                .forResource(ServiceRequest.class)
                .where(ServiceRequest.IDENTIFIER.exactly().identifier(idchidinh))
                .returnBundle(Bundle.class)
                .execute();
        ServiceRequest id = new ServiceRequest();
        if(results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components=  results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id= (ServiceRequest) component.getResource();
            }
        }
        return id;
    }

    public ImagingStudy getImagingStudyByUUID(String uuid) {
        ImagingStudy imagingStudy = null;
        try {
            FHIRtype FHIRtype = new FHIRtype();
            client = FHIRtype.connectFHIR();
            imagingStudy = client.read().resource(ImagingStudy.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch ImagingStudy By uuid {} error!", uuid);
        }
        return imagingStudy;
    }
}
