package inext.ris.order.xray.hl7;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.TX;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.OBX;
import org.springframework.scheduling.annotation.Async;


public class ORUMessageImpl implements ORUMessage {
    private static ORU_R01 oru;
    private static ORU_R01_ORDER_OBSERVATION orderOBR;
    private static ORU_R01_ORDER_OBSERVATION reportOBR;
    private static List<String> segment = new ArrayList<String>();
    private static List<String> segment1 = new ArrayList<String>();
    static Charset windows1252 = Charset.forName("windows-1252");

    public static void main(String[] args) throws HL7Exception, IOException {
        List<OBXModel> obxModelList = new ArrayList<>();
		OBXModel obxModel0 = new OBXModel();
		obxModel0.setIndex(0);
		obxModel0.setType("TX");
		obxModel0.setObs_iden("MG001");
		obxModel0.setObs_sub("");
		obxModel0.setObs_val("");
		obxModel0.setUnits("Date: 12/06/2022  16:20:00 PM");
        OBXModel obxModel1 = new OBXModel();
        obxModel1.setIndex(1);
        obxModel1.setType("TX");
        obxModel1.setObs_iden("MG001");
        obxModel1.setObs_sub("");
        obxModel1.setObs_val("");
        obxModel1.setUnits(" Không thấy vôi hóa nghi ngờ ác tính hay xáo trộn cấu trúc chủ mô tuyến hai bên. ");
        OBXModel obxModel2 = new OBXModel();
        obxModel2.setIndex(2);
        obxModel2.setType("ST");
        obxModel2.setObs_iden("RPT^File Location");
        obxModel2.setObs_sub("1");
        obxModel2.setObs_val("«PLINK2:pictures");
        obxModel2.setUnits("W:/PICTURES LDC/GI endoscopy LDC/TRAN THI HA LOAN 003625 DR.TUO.pdf»");

		obxModelList.add(obxModel0);
        obxModelList.add(obxModel1);
        obxModelList.add(obxModel2);
        //ORUMessageImpl.setOrder("1335656","37.2A02.2","Chụp Xquang ngực thẳng","20220109091100","Viêm dây thần kinh liên sườn/ Hậu COVID T12/2021/ Viêm dạ dày; M2 AZ 24/9, F0 âm tính ngày 14/12/2021; ","BKPACS","CR");
        /*ORUMessageImpl.setReport("251534", "US011", "Siêu âm tổng quát", "20221118161000"
                , "Đau bụng không xác định và đau bụng khác", "TUO", "Tuong"
                , "Nguyen Vinh", "GI", "20221118161500", "US", obxModelList);
        System.out.println(ORUMessageImpl.getTest(4));*/
    }

    public static void setOrder(String accessionNumber, String code, String procedure, String recordedDateTime, String reason, String destination, String modality) {
        ORUMessageImpl.oru = new ORU_R01();
        try {
            oru.initQuickstart("ORU", "R01", "P");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ORUMessageImpl.orderOBR = oru.getPATIENT_RESULT().getORDER_OBSERVATION();
        //Segment OBR
        try {
            OBR obrSegment = orderOBR.getOBR();
            obrSegment.getObr1_SetIDOBR().setValue("");
            obrSegment.getObr2_PlacerOrderNumber().getEntityIdentifier().setValue(accessionNumber);
            obrSegment.getObr3_FillerOrderNumber().getEntityIdentifier().setValue(accessionNumber);
            obrSegment.getObr4_UniversalServiceIdentifier().getAlternateIdentifier().setValue(code);
            obrSegment.getObr4_UniversalServiceIdentifier().getAlternateText().setValue(procedure);
            obrSegment.getObr5_PriorityOBR().setValue("");
            obrSegment.getObr6_RequestedDateTime().getTime().setValue(recordedDateTime);
            obrSegment.getObr7_ObservationDateTime();
            obrSegment.getObr8_ObservationEndDateTime();
            obrSegment.getObr9_CollectionVolume();
            obrSegment.getObr10_CollectorIdentifier();
            obrSegment.getObr11_SpecimenActionCode();
            obrSegment.getObr12_DangerCode();
            obrSegment.getObr13_RelevantClinicalInformation().setValue(reason);
            obrSegment.getObr14_SpecimenReceivedDateTime();
            obrSegment.getObr15_SpecimenSource();
            obrSegment.getObr16_OrderingProvider(0).getSecondAndFurtherGivenNamesOrInitialsThereof().setValue("#");
            obrSegment.getObr17_OrderCallbackPhoneNumber();
            obrSegment.getObr18_PlacerField1().setValue(accessionNumber);
            obrSegment.getObr19_PlacerField2().setValue(accessionNumber);
            obrSegment.getObr20_FillerField1().setValue(destination);
            obrSegment.getObr21_FillerField2();
            obrSegment.getObr22_ResultsRptStatusChngDateTime().getDegreeOfPrecision().setValue("SCHEDULED");
            obrSegment.getObr23_ChargeToPractice();
            obrSegment.getObr24_DiagnosticServSectID().setValue(modality);
            obrSegment.getObr25_ResultStatus();
            obrSegment.getObr26_ParentResult();
            obrSegment.getObr27_QuantityTiming();
            obrSegment.getObr28_ResultCopiesTo();
            obrSegment.getObr29_ParentNumber();
            obrSegment.getObr30_TransportationMode();
            obrSegment.getObr31_ReasonForStudy();
            obrSegment.getObr32_PrincipalResultInterpreter();
            obrSegment.getObr33_AssistantResultInterpreter();
            obrSegment.getObr34_Technician();
            obrSegment.getObr35_Transcriptionist();
            obrSegment.getObr36_ScheduledDateTime();
            obrSegment.getObr37_NumberOfSampleContainers();
            obrSegment.getObr38_TransportLogisticsOfCollectedSample();
            obrSegment.getObr39_CollectorSComment();
            obrSegment.getObr40_TransportArrangementResponsibility();
            obrSegment.getObr41_TransportArranged();
            obrSegment.getObr42_EscortRequired();
            obrSegment.getObr43_PlannedPatientTransportComment();
            obrSegment.getObr44_ProcedureCode();
            obrSegment.getObr45_ProcedureCodeModifier(0).getAlternateText().setValue("");
            ;
            //obrSegment.insertRepetition(45, 0);
            // Now, let's encode the message and look at the output
            segment = Arrays.asList(oru.encode().split("\r"));
            //segment.remove(obrSegment.getObr4_UniversalServiceID().getIdentifier());
        } catch (HL7Exception he) {
            he.fillInStackTrace();
        }
    }

    @Override
    public String getOBR() {
        // TODO Auto-generated method stub
        String message = null;
        message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
        message = message.replace("|^^^", "|");
        message = message.replace("|^SCHEDULED", "|SCHEDULED");
        return message;
    }

    @Async
    public  void setReport(String accessionNumber, String code, String procedure, String recordedDateTime
            , String reason, String provider, String lastname, String name, String position, String resultsDateTime
            , String modality, List<OBXModel> obxList) {
        ORUMessageImpl.oru = new ORU_R01();
        try {
            oru.initQuickstart("ORU", "R01", "T");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ORUMessageImpl.reportOBR = oru.getPATIENT_RESULT().getORDER_OBSERVATION();
        //Segment OBR
        try {
            OBR obrSegment = reportOBR.getOBR();
            obrSegment.getObr1_SetIDOBR().setValue("1");
            obrSegment.getObr2_PlacerOrderNumber().getEntityIdentifier().setValue(accessionNumber);
            obrSegment.getObr3_FillerOrderNumber().getEntityIdentifier().setValue(accessionNumber);
            obrSegment.getObr4_UniversalServiceIdentifier().getAlternateIdentifier().setValue(code);
            obrSegment.getObr4_UniversalServiceIdentifier().getAlternateText().setValue(procedure);
            obrSegment.getObr5_PriorityOBR().setValue("");
            obrSegment.getObr6_RequestedDateTime();
            obrSegment.getObr7_ObservationDateTime().getTime().setValue(recordedDateTime);
            obrSegment.getObr8_ObservationEndDateTime();
            obrSegment.getObr9_CollectionVolume();
            obrSegment.getObr10_CollectorIdentifier();
            obrSegment.getObr11_SpecimenActionCode();
            obrSegment.getObr12_DangerCode();
            obrSegment.getObr13_RelevantClinicalInformation().setValue(reason);
            obrSegment.getObr14_SpecimenReceivedDateTime();
            obrSegment.getObr15_SpecimenSource();
            obrSegment.getObr16_OrderingProvider(0).getIDNumber().setValue(provider);
            obrSegment.getObr16_OrderingProvider(0).getFamilyName().getFn1_Surname().setValue(lastname);
            obrSegment.getObr16_OrderingProvider(0).getGivenName().setValue(name);
            obrSegment.getObr16_OrderingProvider(0).getSecondAndFurtherGivenNamesOrInitialsThereof();
            obrSegment.getObr16_OrderingProvider(0).getSuffixEgJRorIII();
            obrSegment.getObr16_OrderingProvider(0).getPrefixEgDR().setValue("#");
            obrSegment.getObr16_OrderingProvider(0).getDegreeEgMD().setValue(position);
            obrSegment.getObr17_OrderCallbackPhoneNumber();
            obrSegment.getObr18_PlacerField1().setValue(accessionNumber);
            obrSegment.getObr19_PlacerField2().setValue(accessionNumber);
            obrSegment.getObr20_FillerField1();
            obrSegment.getObr21_FillerField2();
            obrSegment.getObr22_ResultsRptStatusChngDateTime().getTime().setValue(resultsDateTime);
            obrSegment.getObr23_ChargeToPractice();
            obrSegment.getObr24_DiagnosticServSectID().setValue(modality);
            obrSegment.getObr25_ResultStatus().setValue("F");
            obrSegment.getObr26_ParentResult();
            obrSegment.getObr27_QuantityTiming();
            obrSegment.getObr28_ResultCopiesTo();
            obrSegment.getObr29_ParentNumber();
            obrSegment.getObr30_TransportationMode();
            obrSegment.getObr31_ReasonForStudy();
            obrSegment.getObr32_PrincipalResultInterpreter();
            obrSegment.getObr33_AssistantResultInterpreter();
            obrSegment.getObr34_Technician();
            obrSegment.getObr35_Transcriptionist();
            obrSegment.getObr36_ScheduledDateTime();
            obrSegment.getObr37_NumberOfSampleContainers();
            obrSegment.getObr38_TransportLogisticsOfCollectedSample();
            obrSegment.getObr39_CollectorSComment();
            obrSegment.getObr40_TransportArrangementResponsibility();
            obrSegment.getObr41_TransportArranged();
            obrSegment.getObr42_EscortRequired();
            obrSegment.getObr43_PlannedPatientTransportComment(0).getIdentifier().setValue("#");
            /*obrSegment.getObr44_ProcedureCode();
            obrSegment.getObr45_ProcedureCodeModifier(0).getAlternateText().setValue("");
            obrSegment.getObr46_PlacerSupplementalServiceInformation();
            obrSegment.getObr47_FillerSupplementalServiceInformation();
            obrSegment.getObr48_MedicallyNecessaryDuplicateProcedureReason();
            obrSegment.getObr49_ResultHandling().setValue("#");*/
            //obrSegment.insertRepetition(45, 0);
            // Now, let's encode the message and look at the output
            segment = Arrays.asList(oru.encode().split("\r"));
        } catch (HL7Exception he) {
            he.fillInStackTrace();
        }
    }
@Async
    public  void setReportOBX(String accessionNumber, String code, String procedure, String recordedDateTime
            , String reason, String provider, String lastname, String name, String position, String resultsDateTime
            , String modality, List<OBXModel> obxList) {
        ORUMessageImpl.oru = new ORU_R01();
        try {
            oru.initQuickstart("ORU", "R01", "T");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ORUMessageImpl.reportOBR = oru.getPATIENT_RESULT().getORDER_OBSERVATION();
        //Segment OBR
        try {
            // Populate the OBX Segment
            OBX obxSegment;

            for (OBXModel obxModel : obxList
            ) {
                obxSegment =  reportOBR.getOBSERVATION(obxModel.getIndex()).getOBX();
                obxSegment.getObx1_SetIDOBX().setValue(Integer.toString(obxModel.getIndex() + 1));
                obxSegment.getObx2_ValueType().setValue(obxModel.getType());
                if (obxModel.getType() == "TX") {
                    obxSegment.getValueType().setValue("TX");
                    TX tx = new TX(oru);
                    tx.setValue(obxModel.getUnits());
                    Varies value = obxSegment.getObservationValue(0);
                    value.setData(tx);
                    obxSegment.getObx3_ObservationIdentifier().getIdentifier().setValue(obxModel.getObs_iden());
                    obxSegment.getObx3_ObservationIdentifier().getNameOfCodingSystem().setValue("#");
                    obxSegment.getObx4_ObservationSubID().setValue(obxModel.getObs_sub());
                } else if (obxModel.getType() == "ST") {
                    obxSegment.getValueType().setValue("ST");
                    TX tx = new TX(oru);
                    tx.setValue(obxModel.getObs_val());
                    Varies value = obxSegment.getObservationValue(0);
                    value.setData(tx);
                    obxSegment.getObx3_ObservationIdentifier().getIdentifier().setValue(obxModel.getObs_iden());
                    obxSegment.getObx4_ObservationSubID().setValue("1");
                    obxSegment.getObx6_Units().getIdentifier().setValue(obxModel.getUnits());
                }
                obxSegment.getObx5_ObservationValue();
                obxSegment.getObx7_ReferencesRange();
                obxSegment.getObx8_AbnormalFlags();
                obxSegment.getObx9_Probability();
                obxSegment.getObx10_NatureOfAbnormalTest();
                obxSegment.getObx11_ObservationResultStatus().setValue("F");
                obxSegment.getObx12_EffectiveDateOfReferenceRange();
                obxSegment.getObx13_UserDefinedAccessChecks();
                obxSegment.getObx14_DateTimeOfTheObservation();
                obxSegment.getObx15_ProducerSID();
                obxSegment.getObx16_ResponsibleObserver();
                obxSegment.getObx17_ObservationMethod();
                obxSegment.getObx19_DateTimeOfTheAnalysis().getTime().setValue("#");
            }
            // Now, let's encode the message and look at the output
            segment1 = Arrays.asList(oru.encode().split("\r"));
        } catch (HL7Exception he) {
            he.fillInStackTrace();
        }
    }

    @Async
    public  void setMessageOBX(List<OBXModel> obxList) {
        ORUMessageImpl.oru = new ORU_R01();
        try {
            oru.initQuickstart("ORU", "R01", "T");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ORUMessageImpl.reportOBR = oru.getPATIENT_RESULT().getORDER_OBSERVATION();
        //Segment OBR
        try {
            // Populate the OBX Segment
            OBX obxSegment;

            for (OBXModel obxModel : obxList
            ) {
                obxSegment =  reportOBR.getOBSERVATION(obxModel.getIndex()).getOBX();
                obxSegment.getObx1_SetIDOBX().setValue(Integer.toString(obxModel.getIndex() + 1));
                obxSegment.getObx2_ValueType().setValue(obxModel.getType());
                if (obxModel.getType() == "TX") {
                    obxSegment.getValueType().setValue("TX");
                    TX tx = new TX(oru);
                    tx.setValue(obxModel.getUnits());
                    Varies value = obxSegment.getObservationValue(0);
                    value.setData(tx);
                    obxSegment.getObx4_ObservationSubID().setValue(obxModel.getObs_sub());
                } else if (obxModel.getType() == "ST") {
                    obxSegment.getValueType().setValue("ST");
                    TX tx = new TX(oru);
                    tx.setValue(obxModel.getObs_val());
                    Varies value = obxSegment.getObservationValue(0);
                    value.setData(tx);
                    obxSegment.getObx4_ObservationSubID().setValue("1");
                    obxSegment.getObx6_Units().getIdentifier().setValue(obxModel.getUnits());
                    obxSegment.getObx14_DateTimeOfTheObservation().getTime().setValue(currentTime());
                }
                obxSegment.getObx5_ObservationValue();
                obxSegment.getObx7_ReferencesRange();
                obxSegment.getObx8_AbnormalFlags();
                obxSegment.getObx9_Probability();
                obxSegment.getObx10_NatureOfAbnormalTest();
                obxSegment.getObx11_ObservationResultStatus().setValue("F");
                obxSegment.getObx12_EffectiveDateOfReferenceRange();
                obxSegment.getObx13_UserDefinedAccessChecks();

                obxSegment.getObx15_ProducerSID();
                obxSegment.getObx16_ResponsibleObserver();
                obxSegment.getObx17_ObservationMethod();
                obxSegment.getObx19_DateTimeOfTheAnalysis().getTime().setValue("#");
            }
            // Now, let's encode the message and look at the output
            segment1 = Arrays.asList(oru.encode().split("\r"));
        } catch (HL7Exception he) {
            he.fillInStackTrace();
        }
    }
    @Override
    public String getOBR_REPORT() {
        // TODO Auto-generated method stub
        String message = null;
        message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
        message = message.replace("|^^^", "|");
        message = message.replace("|^SCHEDULED", "|SCHEDULED");
        return message;
    }
    @Override
    public String getOBX_REPORT(Integer total) {
        String message = "";
        for (int i=2; i <= total; i++) {
            message += segment1.get(i).replaceAll("[\\#\\~\\r\\n]", "") + "\r\n";
        }
        message = message.replace("|^^^", "|");
        message = message.replace("|^SCHEDULED", "|SCHEDULED");
        message = message.replace("\\S\\", "^");
        message = message.replace("\\E", "");
        return message;
    }

    @Override
    public String getOBR_MESSAGE() {
        // TODO Auto-generated method stub
        String message = null;
        message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
        message = message.replace("|^^^", "|");
        message = message.replace("|^SCHEDULED", "|SCHEDULED");
        return message;
    }

    @Override
    public String getOBX_MESSAGE(Integer total) {
        String message = "";
        for (int i=2; i <= total; i++) {
            message += segment1.get(i).replaceAll("[\\#\\~\\r\\n]", "") + "\r\n";
        }
        message = message.replace("|^^^", "|");
        message = message.replace("|^SCHEDULED", "|SCHEDULED");
        message = message.replace("\\S\\", "^");
        message = message.replace("\\E", "");
        return message;
    }

    public static String getTest(Integer total) {
        String message = "";
        for (int i=1; i <= total; i++) {
            message += segment.get(i).replaceAll("[\\#\\~\\r\\n]", "") + "\r\n";
        }
        message = message.replace("|^^^", "|");
        message = message.replace("|^SCHEDULED", "|SCHEDULED");
        message = message.replace("\\S\\", "^");
        return message;
    }

    private static String convertANSI (String content) {
        byte[] bytes = content.getBytes();
        String newFormat = "";
        try {
            newFormat = new String(bytes, windows1252);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return newFormat;
    }

    private static String currentTime (){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
