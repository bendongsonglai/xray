package inext.ris.order.xray.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@RequiredArgsConstructor

public class ADTService {
    @Async
    public   List<String> getADTReport(String send_app, String send_fac, String rece_app, String rece_fac, String dateTimeOfMessage
            , String accessionNumber, String authoredOn, String pid, String family, String given
            , String name, String birthdate, String gender, String race, String address
            , String country, String phoneHome, String phoneBusiness, String ethic, String location
            , String referrer_code, String referrer_name, String referrer_lastname
            , String consult_code, String consult_name, String consult_lastname) {
        String encodedMessage;
        List<String> segment = new ArrayList<String>();
        ADT_A01 adt = new ADT_A01();
        try {
            adt.initQuickstart("ORU", "R01", "T");
            try {
                // Populate the MSH Segment
                MSH mshSegment = adt.getMSH();
                mshSegment.getMsh1_FieldSeparator();
                mshSegment.getMsh2_EncodingCharacters();
                mshSegment.getMsh3_SendingApplication().getNamespaceID().setValue(send_app);
                mshSegment.getMsh4_SendingFacility().getNamespaceID().setValue(send_fac);
                mshSegment.getMsh5_ReceivingApplication().getNamespaceID().setValue(rece_app);
                mshSegment.getMsh6_ReceivingFacility().getNamespaceID().setValue(rece_fac);
                mshSegment.getMsh7_DateTimeOfMessage().getTime().setValue(dateTimeOfMessage);
                mshSegment.getMsh8_Security();
                //mshSegment.getMsh9_MessageType().getMessageCode().setValue("ORU^R01");
                mshSegment.getMsh10_MessageControlID().setValue(accessionNumber);
                mshSegment.getMsh11_ProcessingID().getProcessingID().setValue("P");
                mshSegment.getMsh12_VersionID().getVersionID().setValue("2.3");
                mshSegment.getMsh13_SequenceNumber();
                mshSegment.getMsh14_ContinuationPointer();
                mshSegment.getMsh15_AcceptAcknowledgmentType();
                mshSegment.getMsh16_ApplicationAcknowledgmentType();
                //mshSegment.getMsh17_CountryCode().setValue("VNM");
                //mshSegment.getMsh18_CharacterSet(0).setValue("UNICODE UTF-8");
                mshSegment.getMsh19_PrincipalLanguageOfMessage().getIdentifier().setValue("#");
                //mshSegment.getMsh20_AlternateCharacterSetHandlingScheme();
                //mshSegment.getMsh21_MessageProfileIdentifier(0).getEntityIdentifier().setValue("#");
                // Populate the EVN Segment
                EVN evnSegment = adt.getEVN();
                evnSegment.getEvn1_EventTypeCode().setValue("T02");
                evnSegment.getEvn2_RecordedDateTime().getTime().setValue(authoredOn);
                //evnSegment.getEvn3_DateTimePlannedEvent().getTime().setValue("#");
                //evnSegment.getEvn7_EventFacility().getNamespaceID().setValue("#");
                //evnSegment.insertRepetition(6, 0);
                // Populate the PID Segment
                PID pidSegment = adt.getPID();
                pidSegment.getPid1_SetIDPID().setValue("");
                pidSegment.getPid2_PatientID().getIDNumber().setValue(pid);
                pidSegment.getPid3_PatientIdentifierList();
                pidSegment.getPid4_AlternatePatientIDPID();
                pidSegment.getPid5_PatientName(0).getFamilyName().getSurname().setValue(name.trim());
                pidSegment.getPid5_PatientName(0).getGivenName().setValue(family + given);
                pidSegment.getPid6_MotherSMaidenName();
                pidSegment.getPid7_DateTimeOfBirth().getTime().setValue(birthdate);
                pidSegment.getPid8_AdministrativeSex().setValue(gender);
                pidSegment.getPid9_PatientAlias();
                pidSegment.getPid10_Race(0).getIdentifier().setValue(race);
                pidSegment.getPid11_PatientAddress(0).getStreetAddress().getStreetName().setValue(address);
                pidSegment.getPid12_CountyCode().setValue(country);
                pidSegment.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(phoneHome);
                pidSegment.getPid14_PhoneNumberBusiness(0).getTelephoneNumber().setValue(phoneBusiness);
                pidSegment.getPid15_PrimaryLanguage();
                pidSegment.getPid16_MaritalStatus();
                pidSegment.getPid17_Religion();
                pidSegment.getPid18_PatientAccountNumber();
                pidSegment.getPid19_SSNNumberPatient();
                pidSegment.getPid20_DriverSLicenseNumberPatient();
                pidSegment.getPid21_MotherSIdentifier();
                pidSegment.getPid22_EthnicGroup(0).getIdentifier().setValue(ethic);
                pidSegment.getPid23_BirthPlace();
                pidSegment.getPid24_MultipleBirthIndicator();
                pidSegment.getPid25_BirthOrder();
                pidSegment.getPid26_Citizenship();
                pidSegment.getPid27_VeteransMilitaryStatus();
                pidSegment.getPid28_Nationality();
                pidSegment.getPid29_PatientDeathDateAndTime();
                pidSegment.getPid30_PatientDeathIndicator();
                pidSegment.getPid31_IdentityUnknownIndicator().setValue("#");
                // Populate the PV1 Segment
                String name_ref = referrer_name.substring(referrer_name.length() - 1, referrer_name.length()).contains(" ") ? referrer_name.substring(0, referrer_name.lastIndexOf(" ")) : referrer_name;
                String name_con = consult_name.substring(consult_name.length() - 1, consult_name.length()).contains(" ") ? consult_name.substring(0, consult_name.lastIndexOf(" ")) : consult_name;
                PV1 pv1Segment = adt.getPV1();
                pv1Segment.getPv11_SetIDPV1();
                pv1Segment.getPv12_PatientClass().setValue("O");
                pv1Segment.getPv13_AssignedPatientLocation().getPointOfCare().setValue(location);
                pv1Segment.getPv14_AdmissionType();
                pv1Segment.getPv15_PreadmitNumber();
                pv1Segment.getPv16_PriorPatientLocation();
                pv1Segment.getPv17_AttendingDoctor();
                pv1Segment.getPv18_ReferringDoctor(0).getIDNumber().setValue(referrer_code);
                pv1Segment.getPv18_ReferringDoctor(0).getFamilyName().getSurname().setValue(referrer_lastname.trim());
                pv1Segment.getPv18_ReferringDoctor(0).getGivenName().setValue(name_ref);
                pv1Segment.getPv18_ReferringDoctor(0).getPrefixEgDR().setValue("#");
                pv1Segment.getPv19_ConsultingDoctor(0).getIDNumber().setValue(consult_code);
                pv1Segment.getPv19_ConsultingDoctor(0).getFamilyName().getSurname().setValue(consult_lastname);
                pv1Segment.getPv19_ConsultingDoctor(0).getGivenName().setValue(name_con);
                pv1Segment.getPv19_ConsultingDoctor(0).getPrefixEgDR().setValue("#");
                pv1Segment.getPv110_HospitalService();
                pv1Segment.getPv152_OtherHealthcareProvider(0).getIDNumber().setValue("#");
                // Now, let's encode the message and look at the output
                HapiContext context = new DefaultHapiContext();
                Parser parser = context.getPipeParser();
                encodedMessage = parser.encode(adt);
                segment = Arrays.asList(encodedMessage.split("\r"));
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return segment;
    }
}
