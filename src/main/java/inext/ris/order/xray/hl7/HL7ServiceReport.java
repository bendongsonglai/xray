package inext.ris.order.xray.hl7;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class HL7ServiceReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(HL7ServiceReport.class);

    public static final int DEFAULT_BUFFER_SIZE = 8192;
    static Charset windows1252 = Charset.forName("windows-1252");
    @Async
    public void createReportHL7 (HL7ReportModel hl7) {
        String msh = null;
        String pid = null;
        String evn = null;
        String pv1 = null;
        String orc = null;
        String obr = null;
        String txa = null;
        String obx = null;
        try {
            /*ADTMessageImpl adt = new ADTMessageImpl();

            adt.setADTReport(hl7.getSend_app(), hl7.getSend_fac(), hl7.getRece_app(), hl7.getRece_fac(), hl7.getDateTimeOfMessage()
                    , hl7.getAccessionNumber(), hl7.getAuthoredOn(), hl7.getPid(), hl7.getFamily(), hl7.getGiven()
                    , hl7.getName(), hl7.getBirthdate(), hl7.getGender(), hl7.getRace(), hl7.getAddress()
                    , hl7.getCountry(), hl7.getPhoneHome(), hl7.getPhoneBusiness(), hl7.getEthic(), VNCharacterUtils.removeAccent(hl7.getLocation()).toUpperCase()
                    , hl7.getReferrer_code(), hl7.getReferrer_name(), hl7.getReferrer_lastname()
                    , hl7.getProvider(), hl7.getConsult_name(), hl7.getConsult_lastname());*/
            ADTService adtService = new ADTService();
            List<String> adtSegment = adtService.getADTReport(hl7.getSend_app(), hl7.getSend_fac(), hl7.getRece_app(), hl7.getRece_fac(), hl7.getDateTimeOfMessage()
                    , hl7.getAccessionNumber(), hl7.getAuthoredOn(), hl7.getPid(), hl7.getFamily(), hl7.getGiven()
                    , hl7.getName(), hl7.getBirthdate(), hl7.getGender(), hl7.getRace(), hl7.getAddress()
                    , hl7.getCountry(), hl7.getPhoneHome(), hl7.getPhoneBusiness(), hl7.getEthic(), VNCharacterUtils.removeAccent(hl7.getLocation()).toUpperCase()
                    , hl7.getReferrer_code(), hl7.getReferrer_name(), hl7.getReferrer_lastname()
                    , hl7.getProvider(), hl7.getConsult_name(), hl7.getConsult_lastname());
            ComponentADT componentADT = new ComponentADT();
            String mshSeg=null;
            String evnSeg=null;
            String pidSeg=null;
            String pv1Seg=null;
            try {
              mshSeg = componentADT.getMSH(adtSegment);
              evnSeg = componentADT.getEVN(adtSegment);
              pidSeg = componentADT.getPID(adtSegment);
              pv1Seg = componentADT.getPV1(adtSegment);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ORMMessageImpl orm = new ORMMessageImpl();
            orm.setOrderReport(hl7.getAuthoredOn(), hl7.getReferrer_code()
                    , hl7.getReferrer_name(), hl7.getReferrer_lastname());

            DOCMessageImpl doc = new DOCMessageImpl();
            doc.setDoc(hl7.getDoc_code(), hl7.getDoc_type(), hl7.getActivityDateTime(), hl7.getProvider()
                    , hl7.getConsult_name(), hl7.getConsult_lastname(), hl7.getConsult_position(), hl7.getAccessionNumber(), hl7.getOriginationDateTime()
                    , hl7.getEditDateTime(), hl7.getCe_mp().replaceAll("[\\t\\n\\r]+", " "));

            ORUMessageImpl oru = new ORUMessageImpl();
            oru.setReport(hl7.getAccessionNumber(), hl7.getXray_code(), hl7.getProcedure(), hl7.getRecordedDateTime()
                    , hl7.getReason(), hl7.getProvider(), hl7.getConsult_lastname(), hl7.getConsult_name(), hl7.getConsult_position(), hl7.getResultsDateTime()
                    , hl7.getModality(),  hl7.getObxList());
            oru.setReportOBX(hl7.getAccessionNumber(), hl7.getXray_code(), hl7.getProcedure(), hl7.getRecordedDateTime()
                    , hl7.getReason(), hl7.getProvider(), hl7.getConsult_lastname(), hl7.getConsult_name(), hl7.getConsult_position(), hl7.getResultsDateTime()
                    , hl7.getModality(),  hl7.getObxList());

            SegmentReport segment =  new SegmentReport(mshSeg, evnSeg, pidSeg
                    , pv1Seg, orm.getORCReport(), oru.getOBR_REPORT(), doc.getTXA(), oru.getOBX_REPORT(hl7.getObxList().size() + 1));
            msh = segment.getMsh();
            evn = segment.getEvn();
            pid = segment.getPid();
            pv1 = segment.getPv1();
            orc = segment.getOrc();
            obr = segment.getObrReport();
            txa = segment.getTxa();
            obx = segment.getObx();
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeANSI(hl7.getDirectory()+hl7.getAccessionNumber()+"_"+hl7.getRecordedDateTime()+"_RE.hl7"
                , msh, evn, pid, pv1, orc, obr, txa, obx);

    }


    public static void writeANSI(String fileName,  String msh, String evn, String pid, String pv1, String orc, String obr, String txa, String obx) {

        Path path = Paths.get(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(path, windows1252)) {
            writer.append((msh));
            writer.newLine();
            writer.append((evn));
            writer.newLine();
            writer.append((pid));
            writer.newLine();
            writer.append((pv1));
            writer.newLine();
            writer.append((orc));
            writer.newLine();
            writer.append((obr));
            writer.newLine();
            writer.append((txa));
            writer.newLine();
            writer.append((obx));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            String dir = fileName.substring(0, fileName.lastIndexOf("report")+6);
            String file_copy = fileName.substring(fileName.lastIndexOf("report")+7, fileName.length());
            String file_name = fileName.substring(fileName.lastIndexOf("report")+7, fileName.lastIndexOf(".hl7"));
            String datetime = file_name.split("_")[1];
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            String day = datetime.substring(6, 8);
            String directory = dir.replace("report", "hl7_report_backup") + "/" + year
                    + "/" + month + "/" + day;
            createDirectory(directory);
            File initialFile = new File(fileName);
            InputStream targetStream = FileUtils.openInputStream(initialFile);
            File targetFile = new File(directory + "/" +file_copy);
            copyInputStreamToFile(targetStream, targetFile);
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Cant copy backup report file!");
        }*/

    }


    public static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }

    private static void createDirectory(String dir) {
        try {
            Path path = Paths.get(dir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                LOGGER.info("Create directory {} success!", dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Failed to create directory!" + e.getMessage());
        }
    }
}
