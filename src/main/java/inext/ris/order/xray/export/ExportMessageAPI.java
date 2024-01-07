package inext.ris.order.xray.export;

import inext.ris.order.xray.hl7.*;
import inext.ris.order.xray.hl7.country.Country;
import inext.ris.order.xray.hl7.msh.MSH;
import inext.ris.order.xray.hl7.race.Race;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServicePortal;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.patient_info.PatientInfoModel;
import inext.ris.order.xray.sms.Message;
import inext.ris.order.xray.sms.PortalMessage;
import inext.ris.order.xray.sms.PortalReply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/export_message")
@Slf4j
@RequiredArgsConstructor
public class ExportMessageAPI {
    @RequestMapping(value = "/markisread/{mrn}/{uid}", method = RequestMethod.POST)
    public ResponseEntity<String> isMark(@PathVariable String mrn, @PathVariable String uid) throws InterruptedException {
        exportHL7Mesage(mrn, uid, true);
        return new ResponseEntity<>("Completed mark is read!", HttpStatus.OK);
    }
    @RequestMapping(value = "/reply/{mrn}/{uid}", method = RequestMethod.POST)
    public ResponseEntity<String> reply(@PathVariable String mrn, @PathVariable String uid) throws InterruptedException {
        exportHL7Mesage(mrn, uid, false);
        return new ResponseEntity<>("Completed reply!", HttpStatus.OK);
    }
    private static void exportHL7Mesage(String mrn, String message_id, Boolean isMark) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        WebServicePortal webServicePortal = new WebServicePortal();// cấu hình loại bỏ portal config mà thông qua file cấu hình ngoài
        HL7ReportModel hl7 = new HL7ReportModel();
        try {
            MSH msh = webServiceXray.getMSH();
            //hl7.setDirectory(msh.getDirectory());
            hl7.setDirectory("/home/rispkvic/report/");
            hl7.setSend_app(msh.getSend_app());
            hl7.setSend_fac(msh.getSend_fac());
            hl7.setRece_app(msh.getRece_app());
            hl7.setRece_fac(msh.getRece_fac());

            Message message = webServicePortal.getMessage(message_id);

            PortalMessage portalMessage = webServicePortal.getPortalMessage(message_id);
            String orderDatetime = convertTimestampToStringNormal(message.getCreated());


            hl7.setDateTimeOfMessage(orderDatetime);
            hl7.setAccessionNumber(message_id);
            hl7.setAuthoredOn(orderDatetime);

            hl7.setPid(mrn);
            PatientInfoModel patient = webServiceXray.getPatientByMrn(mrn);
            List<String> patientName = getPatientName(patient.getName());
            hl7.setFamily(patientName.get(0));
            hl7.setGiven(patientName.get(1));
            hl7.setName(patientName.get(2));
            hl7.setBirthdate(convertBirthDate(patient.getBirth_Date()));
            hl7.setGender(patient.getSex());

            Race race = webServiceXray.getRace();
            hl7.setRace(race.getName());
            hl7.setAddress(patient.getAddress());
            Country country = webServiceXray.getCountry();
            hl7.setCountry(country.getAlpha());

            hl7.setPhoneHome(patient.getTelephone());
            hl7.setPhoneBusiness(patient.getTelephone());
            hl7.setEthic(race.getName());
            hl7.setReferrer_code(portalMessage.getPublisher());
            hl7.setReferrer_name(portalMessage.getPhysicans());
            hl7.setReferrer_lastname("");

            String approvedTime = convertTimestampToStringNormal(message.getCreated());

            hl7.setProvider(portalMessage.getPublisher());
            hl7.setConsult_name(portalMessage.getPhysicans());
            hl7.setConsult_lastname("");
            hl7.setConsult_position("");
            hl7.setDateTimeofTransaction(approvedTime);
            if(isMark){
                hl7.setDoc_code("MR");
                hl7.setDoc_type("Mark as Read");
            }else {
                hl7.setDoc_code("PN");
                hl7.setDoc_type("Patient notify");
            }
            hl7.setActivityDateTime(approvedTime);
            hl7.setOriginationDateTime(approvedTime);
            hl7.setEditDateTime(approvedTime);
            if(!isMark) {
                PortalReply portalReply = webServicePortal.getPortalReply(message_id);
                message.setCreated(portalReply.getDatetime());
                message.setDescription(portalReply.getDescription());
            } else {
                message.setCreated(new Date());
                message.setDescription("");
            }
            hl7.setObxList(getOBXList1(portalMessage, message, isMark));
            HL7ServiceMessage hl7Service = new HL7ServiceMessage();
            hl7Service.createHL7Message(hl7);
            log.info("Write report HL7 Message {} completed!", message_id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Write report HL7 Message {} Error!", message_id);
        }
    }

    private static List<OBXModel> getOBXList1(PortalMessage portalMessage, Message message, Boolean isMarkRead) {
        List<OBXModel> obsList = new ArrayList<>();
        if (isMarkRead) {
            return obsList;
        }
        try {
            String description = message.getDescription();
            String[] temps1 = description.split("\n");
            String title = message.getTitle();
            // Title
            for (int i = 0; i < 5; i++) {
                OBXModel obxModelTitle = new OBXModel();
                if (i == 0) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden("");
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Date: " + convertDateTime(message.getCreated()));
                } else if (i == 1) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden("");
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Title: " + title);
                } else if (i == 2) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden("");
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Publisher: " + portalMessage.getPublisher());
                } else if (i == 3) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden("");
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("" + message.getPurpose());
                } else {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden("");
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits(" ");
                }
                obsList.add(obxModelTitle);
            }

            for (int j = 0; j < temps1.length; j++) {
                OBXModel obxModelDesccription = new OBXModel();
                obxModelDesccription.setIndex(j+5);
                obxModelDesccription.setType("TX");
                obxModelDesccription.setObs_iden("");
                obxModelDesccription.setObs_sub("");
                obxModelDesccription.setUnits(temps1[j]);
                obsList.add(obxModelDesccription);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obsList;
    }


    private static String convertTimestampToString(Date timestamp) {
        //System.out.println("before:"+timestamp);
        String datetime = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //newdf.setTimeZone(TimeZone.getTimeZone("UTC"));;
        SimpleDateFormat newdfRp = new SimpleDateFormat("yyyyMMddHHmmss");
        ///newdfRp.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            String temp = newdf.format(timestamp);
            Date tempDate = newdf.parse(temp);
            tempDate = addHoursToJavaUtilDate(tempDate, -7);
            //System.out.println("after:"+tempDate);
            datetime = newdfRp.format(tempDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datetime;
    }

    private static String convertTimestampToStringNormal(Date timestamp) {
        //System.out.println("before:"+timestamp);
        String datetime = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //newdf.setTimeZone(TimeZone.getTimeZone("UTC"));;
        SimpleDateFormat newdfRp = new SimpleDateFormat("yyyyMMddHHmmss");
        ///newdfRp.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            String temp = newdf.format(timestamp);
            Date tempDate = newdf.parse(temp);
            tempDate = addHoursToJavaUtilDate(tempDate, 0);
            //System.out.println("after:"+tempDate);
            datetime = newdfRp.format(tempDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datetime;
    }

    private static List<String> getPatientName(String patientName) {
        patientName = patientName == null ? null :
                Normalizer.normalize(patientName, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        List<String> list = new ArrayList<String>();
        int start;
        int end;
        String family = null;
        String name = null;
        String given = null;
        if (patientName.contains(" ")) {
            start = patientName.indexOf(" ");
            end = patientName.lastIndexOf(" ");
            family = patientName.substring(0, start);
            name = patientName.substring(end, patientName.length());
            given = patientName.substring(start, end);
        } else {
            family = "";
            given = "";
            name = patientName;
        }
        list.add(family);
        list.add(given);
        list.add(name);
        return list;
    }

    private static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    private static String convertMesageTime(Date time) {
        String dob = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = addHoursToJavaUtilDate(time, -7);
        try {
            dob = newdf.format(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dob;
    }
    private static String convertBirthDate(Date birthdate) {
        String dob = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyyMMdd");
        birthdate = addHoursToJavaUtilDate(birthdate, 17);
        try {
            dob = newdf.format(birthdate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dob;
    }

    private static String convertDateTime(Date dateReport) {
        String reportTime = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //newdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat newdfRp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        //newdfRp.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            String temp = newdf.format(dateReport);
            Date tempDate = newdf.parse(temp);
            reportTime = newdfRp.format(tempDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reportTime;
    }
}
