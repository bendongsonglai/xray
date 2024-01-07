package inext.ris.order.xray.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v231.group.DOC_T12_EVNPIDPV1TXAOBX;
import ca.uhn.hl7v2.model.v231.message.DOC_T12;
import ca.uhn.hl7v2.model.v231.segment.TXA;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DOCMessageImpl implements DOCMessage {
    private static DOC_T12 doc;
    private static DOC_T12_EVNPIDPV1TXAOBX reportDOC;
    private static List<String> segment = new ArrayList<String>();

    public static void main(String[] args) throws HL7Exception, IOException {
        List<OBXModel> obxModelList = new ArrayList<>();
        OBXModel obxModel1 = new OBXModel();
        obxModel1.setIndex(0);
        obxModel1.setType("TX");
        obxModel1.setObs_iden("MG001");
        obxModel1.setObs_sub("");
        obxModel1.setObs_val("");
        obxModel1.setUnits(" Không thấy vôi hóa nghi ngờ ác tính hay xáo trộn cấu trúc chủ mô tuyến hai bên. ");
        OBXModel obxModel2 = new OBXModel();
        obxModel2.setIndex(1);
        obxModel2.setType("ST");
        obxModel2.setObs_iden("RPT^File Location");
        obxModel2.setObs_sub("1");
        obxModel2.setObs_val("«PLINK2:pictures");
        obxModel2.setUnits("W:/PICTURES LDC/GI endoscopy LDC/TRAN THI HA LOAN 003625 DR.TUO.pdf»");

        obxModelList.add(obxModel1);
        obxModelList.add(obxModel2);

        /*DOCMessageImpl.setDoc("DI","X-Ray","20221211162000","TUO",
                "Nguyen Vinh","Tuong", "GI","251537", "20221211",
                "20221211", "Clinical Elements: MAMMOGRAPHY: BI-RADS 0");
        System.out.println(DOCMessageImpl.getTest());*/
    }
    @Async
    public void setDoc(String doc_code, String doc_type, String activityDateTime, String provider
            , String name, String lastname, String position, String accessionNumber, String originationDateTime
            , String editDateTime, String ce_mp) {
        DOCMessageImpl.doc = new DOC_T12();
        try {
            doc.initQuickstart("DOC", "T12", "P");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DOCMessageImpl.reportDOC = doc.getEVNPIDPV1TXAOBX();

        try {
            // Populate the TXA Segment
            TXA txaSegment = reportDOC.getTXA();
            txaSegment.getTxa1_SetIDTXA().setValue(accessionNumber);
            txaSegment.getTxa2_DocumentType().setValue(doc_code+"^"+doc_type);
            txaSegment.getTxa3_DocumentContentPresentation().setValue("TX");
            txaSegment.getTxa4_ActivityDateTime().getTimeOfAnEvent().setValue(activityDateTime);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getIDNumber().setValue(provider);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getFamilyLastName().getFamilyName().setValue(lastname);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getGivenName().setValue(name);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getMiddleInitialOrName();
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getSuffixEgJRorIII();
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getPrefixEgDR().setValue("#");
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getDegreeEgMD().setValue(position);
            txaSegment.getTxa6_OriginationDateTime().getTimeOfAnEvent().setValue(originationDateTime);
            txaSegment.getTxa7_TranscriptionDateTime();
            txaSegment.getTxa8_EditDateTime(0).getTimeOfAnEvent().setValue(editDateTime);
            txaSegment.getTxa9_OriginatorCodeName();
            txaSegment.getTxa10_AssignedDocumentAuthenticator();
            txaSegment.getTxa11_TranscriptionistCodeName();
            txaSegment.getTxa12_UniqueDocumentNumber().getEntityIdentifier().setValue("");
            txaSegment.getTxa12_UniqueDocumentNumber().getNamespaceID().setValue("");
            txaSegment.getTxa12_UniqueDocumentNumber().getUniversalID().setValue(ce_mp);
            txaSegment.getTxa13_ParentDocumentNumber();
            txaSegment.getTxa14_PlacerOrderNumber();
            txaSegment.getTxa15_FillerOrderNumber();
            txaSegment.getTxa16_UniqueDocumentFileName().setValue(doc_type);
            txaSegment.getTxa17_DocumentCompletionStatus().setValue(doc_code);
            txaSegment.getTxa18_DocumentConfidentialityStatus().setValue("#");
            /*txaSegment.getTxa19_DocumentAvailabilityStatus().setValue("#");
            txaSegment.getTxa20_DocumentStorageStatus();
            txaSegment.getTxa21_DocumentChangeReason();
            txaSegment.getTxa22_AuthenticationPersonTimeStamp();
            txaSegment.getTxa23_DistributedCopiesCodeandNameofRecipients(0).getIDNumber().setValue("#");*/


            // Now, let's encode the message and look at the output
            segment =  Arrays.asList(doc.encode().split("\r"));
        } catch (Exception he) {
            he.printStackTrace();
        }

    }

    @Async
    public void setDocmessage(String doc_code, String doc_type, String activityDateTime, String provider
            , String name, String lastname, String position, String accessionNumber, String originationDateTime
            , String editDateTime) {
        DOCMessageImpl.doc = new DOC_T12();
        try {
            doc.initQuickstart("DOC", "T12", "P");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DOCMessageImpl.reportDOC = doc.getEVNPIDPV1TXAOBX();

        try {
            // Populate the TXA Segment
            TXA txaSegment = reportDOC.getTXA();
            txaSegment.getTxa1_SetIDTXA().setValue(accessionNumber);
            txaSegment.getTxa2_DocumentType().setValue(doc_code+"^"+doc_type);
            txaSegment.getTxa3_DocumentContentPresentation().setValue("TX");
            txaSegment.getTxa4_ActivityDateTime().getTimeOfAnEvent().setValue(activityDateTime);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getIDNumber().setValue(provider+"^"+name);
            /*txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getFamilyLastName().getFamilyName().setValue(lastname);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getGivenName().setValue(name);
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getMiddleInitialOrName();*/
            txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getSuffixEgJRorIII().setValue("#");
            //txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getPrefixEgDR().setValue("#");
            //txaSegment.getTxa5_PrimaryActivityProviderCodeName(0).getDegreeEgMD().setValue(position);
            txaSegment.getTxa6_OriginationDateTime().getTimeOfAnEvent().setValue(originationDateTime);
            txaSegment.getTxa7_TranscriptionDateTime();
            txaSegment.getTxa8_EditDateTime(0).getTimeOfAnEvent().setValue(editDateTime);
            txaSegment.getTxa9_OriginatorCodeName();
            txaSegment.getTxa10_AssignedDocumentAuthenticator();
            txaSegment.getTxa11_TranscriptionistCodeName();
            txaSegment.getTxa12_UniqueDocumentNumber().getEntityIdentifier().setValue("");
            txaSegment.getTxa12_UniqueDocumentNumber().getNamespaceID().setValue("");
            txaSegment.getTxa12_UniqueDocumentNumber().getUniversalID().setValue("");
            txaSegment.getTxa13_ParentDocumentNumber();
            txaSegment.getTxa14_PlacerOrderNumber();
            txaSegment.getTxa15_FillerOrderNumber();
            txaSegment.getTxa16_UniqueDocumentFileName().setValue(doc_type);
            txaSegment.getTxa17_DocumentCompletionStatus().setValue(doc_code);
            txaSegment.getTxa18_DocumentConfidentialityStatus().setValue("#");
            /*txaSegment.getTxa19_DocumentAvailabilityStatus().setValue("#");
            txaSegment.getTxa20_DocumentStorageStatus();
            txaSegment.getTxa21_DocumentChangeReason();
            txaSegment.getTxa22_AuthenticationPersonTimeStamp();
            txaSegment.getTxa23_DistributedCopiesCodeandNameofRecipients(0).getIDNumber().setValue("#");*/


            // Now, let's encode the message and look at the output
            segment =  Arrays.asList(doc.encode().split("\r"));
        } catch (Exception he) {
            he.printStackTrace();
        }

    }

    @Override
    public String getTXA() {
        String message = null;
        message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
        message = message.replace("\\S\\", "^");
        return message;
    }

    @Override
    public List<String> getOBX() {
        return null;
    }

    public static String getTest() {
        String message = null;
        message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
        message = message.replace("\\S\\", "^");
        //message = message.replaceAll("\\X000d", "");
        return message;
    }
}
