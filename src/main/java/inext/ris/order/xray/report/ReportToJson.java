package inext.ris.order.xray.report;

import ca.uhn.fhir.context.FhirContext;
import inext.ris.order.xray.code.CodeModel;
import inext.ris.order.xray.department.DepartmentModel;
import inext.ris.order.xray.patient_info.PatientInfoModel;
import inext.ris.order.xray.referrer.ReferrerModel;
import inext.ris.order.xray.request.RequestModel;
import inext.ris.order.xray.request_detail.RequestDetailModel;
import inext.ris.order.xray.user.UserModel;
import org.hl7.fhir.r4.model.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ReportToJson {
    public String bodyReport(PatientInfoModel patientInfoModel, RequestModel requestModel, CodeModel codeModel, DepartmentModel departmentModel,
                             ReferrerModel referrerModel, RequestDetailModel requestDetailModel, ReportModel reportModel, UserModel userModel) {
        String output = null;
        FhirContext ourCtx = FhirContext.forR4();
        Bundle bundle = new Bundle();
        Patient patientResource = new Patient();
        Encounter encounterResource = new Encounter();
        ServiceRequest serviceRequestResource = new ServiceRequest();
        ImagingStudy imagingStudyResource = new ImagingStudy();
        Media mediaResource = new Media();
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        Practitioner practitionerResource = new Practitioner();
        try {
            /* Start Patient Resource*/
            //System.out.println("Patient: "+patientInfoModel.getMrn());
            patientResource.setId(patientInfoModel.getMrn());
            Narrative narrative = new Narrative();
            narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
            patientResource.setText(narrative);
            List<Identifier> identifierList = new ArrayList<>();
            Identifier identifierP = new Identifier();
            identifierP.setSystem("MA_BN");
            identifierP.setValue(patientInfoModel.getMrn());
            identifierList.add(identifierP);
            patientResource.setIdentifier(identifierList);
            List<HumanName> humanNameList = new ArrayList<>();
            HumanName humanName = new HumanName();
            humanName.setUse(HumanName.NameUse.OFFICIAL);
            humanName.setText(patientInfoModel.getName());
            humanNameList.add(humanName);
            patientResource.setName(humanNameList);
            if(patientInfoModel.getSex().equals("F")) {
                patientResource.setGender(Enumerations.AdministrativeGender.FEMALE);
            } else  {
                patientResource.setGender(Enumerations.AdministrativeGender.MALE);
            }
            patientResource.setBirthDate(patientInfoModel.getBirth_Date());
            Type booleanType = new BooleanType(false);
            patientResource.setDeceased(booleanType);
            List<Address> addressList = new ArrayList<>();
            Address address = new Address();
            address.setUse(Address.AddressUse.HOME);
            address.setText(patientInfoModel.getAddress());
            addressList.add(address);
            patientResource.setAddress(addressList);
            /* Finish Patient Resource*/
            /* Start Encounter Resource*/
            encounterResource.setId(codeModel.getXray_code());
            List<Identifier> identifierListEn = new ArrayList<>();
            Identifier identifierEn = new Identifier();
            identifierEn.setValue(codeModel.getXray_code());
            identifierListEn.add(identifierEn);
            encounterResource.setIdentifier(identifierListEn);
            encounterResource.setStatus(Encounter.EncounterStatus.INPROGRESS);
            Coding codingEn = new Coding();
            codingEn.setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode");
            codingEn.setCode("IMP");
            codingEn.setDisplay("Khám bệnh");
            encounterResource.setClass_(codingEn);
            Period periodEn = new Period();
            periodEn.setStart(requestModel.getRequest_timestamp());
            encounterResource.setPeriod(periodEn);
            List<Encounter.EncounterLocationComponent> locationComponentList  = new ArrayList<>();
            Encounter.EncounterLocationComponent locationComponent = new Encounter.EncounterLocationComponent();
            Reference referenceEn = new Reference();
            referenceEn.setReference(departmentModel.getName_vie());
            locationComponent.setLocation(referenceEn);
            locationComponentList.add(locationComponent);
            encounterResource.setLocation(locationComponentList);
            /* Finish Enconter Resource*/
            /* Start ServiceRequest Resource*/
            serviceRequestResource.setId(requestModel.getRequest_no());
            serviceRequestResource.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
            serviceRequestResource.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
            serviceRequestResource.setPriority(ServiceRequest.ServiceRequestPriority.ROUTINE);
            CodeableConcept codeableConceptSer = new CodeableConcept();
            List<Coding> codingSers = new ArrayList<>();
            Coding codingSer = new Coding();
            codingSer.setSystem("local");
            codingSer.setCode(codeModel.getXray_code());
            codingSers.add(codingSer);
            codeableConceptSer.setCoding(codingSers);
            codeableConceptSer.setText(codeModel.getDescription());
            serviceRequestResource.setCode(codeableConceptSer);
            Reference referenceSer = new Reference();
            referenceSer.setReference("Patient/"+requestModel.getMrn());
            serviceRequestResource.setSubject(referenceSer);
            serviceRequestResource.setAuthoredOn(requestModel.getRequest_timestamp());
            Reference referenceSer1 = new Reference();
            referenceSer1.setReference("Practitioner/"+referrerModel.getPractitioner_id());
            referenceSer1.setDisplay(referrerModel.getName()+referrerModel.getLastname());
            serviceRequestResource.setRequester(referenceSer1);
            CodeableConcept codeableConceptSer1 = new CodeableConcept();
            codeableConceptSer1.setText(requestModel.getNote());
            List<CodeableConcept> reasons = new ArrayList<>();
            reasons.add(codeableConceptSer1);
            serviceRequestResource.setReasonCode(reasons);
            /* Finish ServiceRequest Resource*/
            /* Start ImagingStudy Resource*/
            imagingStudyResource.setId(requestDetailModel.getAccession());
            Reference referenceImg = new Reference();
            referenceImg.setReference("Patient/"+requestModel.getMrn());
            imagingStudyResource.setSubject(referenceImg);
            imagingStudyResource.setNumberOfInstances(1);
            List<ImagingStudy.ImagingStudySeriesComponent> imagingStudySeriesComponentList =  new ArrayList<>();
            ImagingStudy.ImagingStudySeriesComponent imagingStudySeriesComponent = new ImagingStudy.ImagingStudySeriesComponent();
            Coding codingImg = new Coding();
            codingImg.setCode(codeModel.getXray_type_code());
            imagingStudySeriesComponent.setModality(codingImg);
            imagingStudySeriesComponent.setNumberOfInstances(1);
            List<ImagingStudy.ImagingStudySeriesPerformerComponent> imagingStudySeriesPerformerComponents = new ArrayList<>();
            ImagingStudy.ImagingStudySeriesPerformerComponent imagingStudySeriesPerformerComponent = new ImagingStudy.ImagingStudySeriesPerformerComponent();
            CodeableConcept codeableConceptImg1 = new CodeableConcept();
            Coding codingImg1 = new Coding();
            codingImg1.setSystem("http://terminology.hl7.org/CodeSystem/v3-ParticipationType");
            codingImg1.setCode("SPRF");
            List<Coding> codingImg1s = new ArrayList<>();
            codingImg1s.add(codingImg1);
            codeableConceptImg1.setCoding(codingImg1s);
            imagingStudySeriesPerformerComponent.setFunction(codeableConceptImg1);
            Reference referenceImg1 = new Reference();
            referenceImg1.setDisplay(userModel.getLogin() + "|"+userModel.getName());
            imagingStudySeriesPerformerComponent.setActor(referenceImg1);
            imagingStudySeriesPerformerComponents.add(imagingStudySeriesPerformerComponent);
            imagingStudySeriesComponent.setPerformer(imagingStudySeriesPerformerComponents);
            imagingStudySeriesComponentList.add(imagingStudySeriesComponent);
            imagingStudyResource.setSeries(imagingStudySeriesComponentList);
            /* Finish ImagingStudy Resource*/
            /* Start Media Resource*/
            mediaResource.setId(requestDetailModel.getAccession());
            Reference referenceMedia= new Reference();
            referenceMedia.setReference("Patient/"+requestModel.getMrn());
            mediaResource.setSubject(referenceMedia);
            mediaResource.setDeviceName("http:\\local_IP_RIS:portRIS/AutoSearch/AutoSearch?accessionno="+requestDetailModel.getAccession());
            Attachment attachment = new Attachment();
            attachment.setContentType("application/xml");
            String value = Base64.getEncoder().encodeToString("raw data digital signatures".getBytes());
            attachment.setData(value.getBytes(StandardCharsets.UTF_8));
            mediaResource.setContent(attachment);
            /* Finish Media Resource*/
            /* Start DiagnosticReport Resource*/
            Narrative narrative1 = new Narrative();
            narrative1.setStatus(Narrative.NarrativeStatus.EMPTY);
            diagnosticReport.setText(narrative1);
            Identifier identifierDiag = new Identifier();
            identifierDiag.setValue(requestDetailModel.getAccession());
            List<Identifier> identifierDiagList = new ArrayList<>();
            identifierDiagList.add(identifierDiag);
            diagnosticReport.setIdentifier(identifierDiagList);
            diagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
            Reference referenceDiag= new Reference();
            referenceDiag.setReference("Patient/"+requestModel.getMrn());
            diagnosticReport.setSubject(referenceDiag);
            Period typeTime = new Period();
            typeTime.setStart(requestDetailModel.getAuto_save_time());
            typeTime.setEnd(requestDetailModel.getAssign_time());
            diagnosticReport.setEffective(typeTime);
            diagnosticReport.setIssued(requestModel.getRequest_timestamp());
            List<Reference> diagReferenceDiagList = new ArrayList<>();
            Reference referenceDiag1 = new Reference();
            referenceDiag1.setReference("Practitioner/"+userModel.getLogin());
            Identifier identifierDiag1 = new Identifier();
            identifierDiag1.setSystem("CCHN");
            identifierDiag1.setValue(userModel.getDf_code());
            referenceDiag1.setIdentifier(identifierDiag1);
            referenceDiag1.setDisplay(userModel.getName());
            diagReferenceDiagList.add(referenceDiag1);
            diagnosticReport.setPerformer(diagReferenceDiagList);
            List<Reference> referenceResultList = new ArrayList<>();
            Reference referenceDiag2 = new Reference();
            referenceDiag2.setDisplay(reportModel.getDescribereport());
            referenceResultList.add(referenceDiag2);
            diagnosticReport.setResult(referenceResultList);
            List<Reference> referenceImgList = new ArrayList<>();
            Reference referenceImaging = new Reference();
            referenceImaging.setReference("ImagingStudy/"+requestDetailModel.getAccession());
            referenceImgList.add(referenceImaging);
            diagnosticReport.setImagingStudy(referenceImgList);
            List<DiagnosticReport.DiagnosticReportMediaComponent> diagnosticReportMediaComponentList = new ArrayList<>();
            DiagnosticReport.DiagnosticReportMediaComponent diagnosticReportMediaComponent = new DiagnosticReport.DiagnosticReportMediaComponent();
            diagnosticReportMediaComponent.setComment("Download File raw data Signatured!");
            Reference referenceLinkMedia = new Reference();
            referenceLinkMedia.setReference("Media/"+requestDetailModel.getAccession());
            diagnosticReportMediaComponent.setLink(referenceLinkMedia);
            diagnosticReportMediaComponentList.add(diagnosticReportMediaComponent);
            diagnosticReport.setMedia(diagnosticReportMediaComponentList);
            diagnosticReport.setConclusion(reportModel.getConclusion());
            /* Finish DiagnosticReport Resource*/
            /* Start Practitioner Resource*/
            practitionerResource.setId(userModel.getLogin());
            List<Identifier> identifierPracList = new ArrayList<>();
            Identifier identifierPrac= new Identifier();
            identifierPrac.setSystem("CCHN");
            identifierPrac.setValue(userModel.getDf_code());
            identifierPracList.add(identifierPrac);
            practitionerResource.setIdentifier(identifierPracList);
            List<HumanName> humanNamePracList = new ArrayList<>();
            HumanName humanNamePrac = new HumanName();
            humanNamePrac.setUse(HumanName.NameUse.OFFICIAL);
            humanNamePrac.setText(userModel.getName());
            humanNamePracList.add(humanNamePrac);
            practitionerResource.setName(humanNamePracList);
            /* Finish Practitioner Resource*/

            Bundle.BundleEntryComponent entry1 = bundle.addEntry().setResource(patientResource);
            Bundle.BundleEntryComponent entry2 = bundle.addEntry().setResource(encounterResource);
            Bundle.BundleEntryComponent entry3 = bundle.addEntry().setResource(serviceRequestResource);
            Bundle.BundleEntryComponent entry4 = bundle.addEntry().setResource(imagingStudyResource);
            Bundle.BundleEntryComponent entry5 = bundle.addEntry().setResource(mediaResource);
            Bundle.BundleEntryComponent entry6 = bundle.addEntry().setResource(diagnosticReport);
            Bundle.BundleEntryComponent entry7 = bundle.addEntry().setResource(practitionerResource);
            bundle.setType(Bundle.BundleType.COLLECTION);
            Identifier identifierBunlde = new Identifier();
            identifierBunlde.setSystem("Accession-number");
            identifierBunlde.setValue(requestDetailModel.getAccession());
            bundle.setIdentifier(identifierBunlde);
            output = ourCtx.newJsonParser().encodeResourceToString(bundle);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    public static void main(String[] args) {

    }
}
