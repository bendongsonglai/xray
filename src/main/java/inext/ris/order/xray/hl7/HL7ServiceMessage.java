package inext.ris.order.xray.hl7;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Slf4j
public class HL7ServiceMessage {
    @Async
    public void createHL7Message (HL7ReportModel hl7) {
        String msh = null;
        String pid = null;
        String evn = null;
        String txa = null;
        String obx = null;
        try {
            ADTMessageService adtService = new ADTMessageService();
            List<String> adtSegment = adtService.getADTMessage(hl7.getSend_app(), hl7.getSend_fac(), hl7.getRece_app(), hl7.getRece_fac(), hl7.getDateTimeOfMessage()
                    , hl7.getAccessionNumber(), hl7.getAuthoredOn(), hl7.getPid(), hl7.getFamily(), hl7.getGiven()
                    , hl7.getName(), hl7.getBirthdate(), hl7.getGender(), hl7.getRace(), hl7.getAddress()
                    , hl7.getCountry(), hl7.getPhoneHome(), hl7.getPhoneBusiness(), hl7.getEthic(), VNCharacterUtils.removeAccent("").toUpperCase()
                    , hl7.getReferrer_code(), hl7.getReferrer_name(), hl7.getReferrer_lastname()
                    , hl7.getProvider(), hl7.getConsult_name(), hl7.getConsult_lastname());
            ComponentADT componentADT = new ComponentADT();
            String mshSeg=null;
            String evnSeg=null;
            String pidSeg=null;
            try {
                mshSeg = componentADT.getMSH(adtSegment);
                evnSeg = componentADT.getEVN(adtSegment);
                pidSeg = componentADT.getPID(adtSegment);
            } catch (Exception e) {
                e.printStackTrace();
            }

            DOCMessageImpl doc = new DOCMessageImpl();
            doc.setDocmessage(hl7.getDoc_code(), hl7.getDoc_type(), hl7.getActivityDateTime(), hl7.getProvider()
                    , hl7.getConsult_name(), hl7.getConsult_lastname(), hl7.getConsult_position(), hl7.getAccessionNumber(), hl7.getOriginationDateTime()
                    , hl7.getEditDateTime());

            ORUMessageImpl oru = new ORUMessageImpl();
            oru.setMessageOBX(hl7.getObxList());

            SegmentMessage segment =  new SegmentMessage(mshSeg, evnSeg, pidSeg
                    , doc.getTXA(), oru.getOBX_MESSAGE(hl7.getObxList().size() + 1));
            msh = segment.getMsh();
            evn = segment.getEvn();
            pid = segment.getPid();
            txa = segment.getTxa();
            obx = segment.getObx();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(hl7.getObxList().size() == 0){
            writeANSI(hl7.getDirectory()+"MARK_"+hl7.getAccessionNumber()+"_"+currentTime()+".hl7"
                    , msh, evn, pid, txa, obx);
        } else {
            writeANSI(hl7.getDirectory() + "MSG_" + hl7.getAccessionNumber() + "_" + currentTime() + ".hl7"
                    , msh, evn, pid, txa, obx);
        }

    }


    public static void writeANSI(String fileName,  String msh, String evn, String pid, String txa, String obx) {

        Path path = Paths.get(fileName);
        log.info("fileName Message:"+fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.append((msh));
            writer.newLine();
            writer.append((evn));
            writer.newLine();
            writer.append((pid));
            writer.newLine();
            writer.append((txa));
            writer.newLine();
            writer.append((obx));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String currentTime (){
        SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMddHHmmss");
        Date date = new Date();
        try {
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
