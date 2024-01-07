package inext.ris.order.xray.export;

import inext.ris.order.xray.code.CodeModel;
import inext.ris.order.xray.department.DepartmentModel;
import inext.ris.order.xray.hl7.HL7ReportModel;
import inext.ris.order.xray.hl7.HL7Service;
import inext.ris.order.xray.hl7.OBXModel;
import inext.ris.order.xray.hl7.VNCharacterUtils;
import inext.ris.order.xray.hl7.country.Country;
import inext.ris.order.xray.hl7.msh.MSH;
import inext.ris.order.xray.hl7.obx.OBX;
import inext.ris.order.xray.hl7.race.Race;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.patient_info.PatientInfoModel;
import inext.ris.order.xray.referrer.ReferrerModel;
import inext.ris.order.xray.report.ReportPublic;
import inext.ris.order.xray.report.title.Title;
import inext.ris.order.xray.request.RequestModel;
import inext.ris.order.xray.request_detail.RequestDetailModel;
import inext.ris.order.xray.type.TypeModel;
import inext.ris.order.xray.user.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/export")
@Slf4j
@RequiredArgsConstructor
public class ExportReportAPI {
    @Autowired
    ExportReportService exportReportService;

    public static final int DEFAULT_BUFFER_SIZE = 8192;
    @RequestMapping(value = "/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> create(@PathVariable String acc) throws InterruptedException {
        exportReportService.exportHL7(acc);
        Thread.sleep(2000);
        return new ResponseEntity<>("Completed!", HttpStatus.OK);
    }

    @RequestMapping(value = "/validate/temp", method = RequestMethod.POST)
    public ResponseEntity<String> checkValidate()  {
        List<String> reportTemps = new ArrayList<>();
        String directory = "/home/rispkvic/reporttemp/";
        Boolean check;
        try {
            reportTemps = getHL7Temps("/home/rispkvic/reporttemp/");
            for (String reportDir: reportTemps
            ) {
                check = validateReport(directory+reportDir);
                if(check) {
                    publicReport(directory+reportDir);
                } else {
                    String acc = reportDir.substring(0, reportDir.indexOf("_"));
                    tempReportError(directory+reportDir);
                    CreateProcessHL7("http://172.16.100.4:8093/api/v1/export/process/"+acc+"/INEXT");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Validate report bad!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Validate report good!", HttpStatus.OK);
    }

    @RequestMapping(value = "/process/{acc}/{user}", method = RequestMethod.POST)
    public ResponseEntity<String> createProcess(@PathVariable String acc, @PathVariable String user)  {
        String filename = UUID.randomUUID().toString();
        try {
            FileOutputStream fos = new FileOutputStream("/home/rissv/request/" + user+"_"+filename + ".txt");
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(acc);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Write request approved error!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Write request approved success!", HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> createReExport(@PathVariable String acc) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        HL7ReportModel hl7 = new HL7ReportModel();
        try {
            MSH msh = webServiceXray.getMSH();
            hl7.setDirectory(msh.getDirectory().replace("reporttemp", "re_report"));
            //hl7.setDirectory("D:\\Disk_D\\Workspace Victoria\\report\\");
            hl7.setSend_app(msh.getSend_app());
            hl7.setSend_fac(msh.getSend_fac());
            hl7.setRece_app(msh.getRece_app());
            hl7.setRece_fac(msh.getRece_fac());

            RequestModel request = webServiceXray.getRequestByAcc(acc);
            String orderDatetime = convertTimestampToString(request.getRequest_timestamp());
            String mrn = request.getMrn();

            hl7.setDateTimeOfMessage(orderDatetime);
            hl7.setAccessionNumber(acc);
            hl7.setAuthoredOn(orderDatetime);

            hl7.setPid(request.getMrn());
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
            DepartmentModel department;
            try {
                department = webServiceXray.getDepartmentBydepartID(request.getDepartment_id());
            } catch (Exception e) {
                e.printStackTrace();
                department = webServiceXray.getDepartmentBydepartID("001");
            }
            hl7.setLocation(department.getName_vie());

            ReferrerModel referrer = webServiceXray.getRefererrerByRefID(request.getReferrer());
            hl7.setReferrer_code(referrer.getPrefix());
            hl7.setReferrer_name(referrer.getName_eng());
            hl7.setReferrer_lastname(referrer.getLastname_eng());

            RequestDetailModel requestDetail = webServiceXray.getRequestDetailsByAcc(acc);
            String radiologist = requestDetail.getAssign();
            String provider = referrer.getPrefix();

            String approvedTime = convertTimestampToString(requestDetail.getApproved_time());
            approvedTime = checkBefore(requestDetail.getApproved_time(), request.getRequest_timestamp()) ? add5Minute(orderDatetime) : approvedTime;
            UserModel user = webServiceXray.getUserByCode(radiologist);
            hl7.setProvider(referrer.getPrefix());
            //hl7.setConsult_name(user.getName_eng());
            //hl7.setConsult_lastname(user.getLastname_eng());
            hl7.setConsult_name(referrer.getName_eng());
            hl7.setConsult_lastname(referrer.getLastname_eng());
            hl7.setConsult_position("");
            hl7.setDateTimeofTransaction(approvedTime);

            CodeModel code = webServiceXray.getXrayCodeByCode(requestDetail.getXray_code());
            TypeModel type = webServiceXray.getType(code.getXray_type_code());
            hl7.setDoc_code("DI");
            hl7.setDoc_type(type.getDetail());
            hl7.setActivityDateTime(approvedTime);
            hl7.setOriginationDateTime(approvedTime);
            hl7.setEditDateTime(approvedTime);

            ReportPublic report = webServiceXray.getReportDetails(acc);
            String describe = report.getDescribe();
            String conclusion = report.getConclusion();
            String note = report.getNote();
            hl7.setCe_mp(getCe_Mp(conclusion));
            hl7.setXray_code(code.getXray_code());
            hl7.setProcedure(code.getDescription());
            hl7.setRecordedDateTime(approvedTime);
            hl7.setReason(request.getNote());
            hl7.setResultsDateTime(approvedTime);
            hl7.setModality(code.getXray_type_code());

            Title title = webServiceXray.getTitleReport(code.getXray_code());
            ReportSegment reportSegment = new ReportSegment();
            reportSegment.setDateTime(convertDateTime(requestDetail.getApproved_time()));
            reportSegment.setTitle(title == null ? "" : VNCharacterUtils.removeAccent(title.getDes_vn()));
            reportSegment.setProvider(provider);
            reportSegment.setRadiogist(radiologist);
            reportSegment.setXray_code(code.getXray_code());
            reportSegment.setSign(user.getName_eng() + " " + user.getLastname_eng());
            reportSegment.setDescribe(describe);
            //reportSegment.setConclusion(position(conclusion) == 10000 ? conclusion : conclusion.substring(0, position(conclusion)));
            reportSegment.setConclusion(conclusion);

            reportSegment.setNote(note != null ? note : "\n");

            OBX obx = webServiceXray.getOBX();
            obx.setUnits(obx.getUnits() + mrn + "_" + acc + ".pdf");
            //hl7.setObxList(getOBXList(reportSegment, obx));
            hl7.setObxList(getOBXList1(reportSegment, obx));
            HL7Service hl7Service = new HL7Service();
            hl7Service.createReportHL7(hl7);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Completed!", HttpStatus.OK);
    }

    @RequestMapping(value = "/pdf/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> copyPDF(@PathVariable String acc) {
        InputStream targetStream = null;
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        String mrn = "";
        try {
            RequestModel request = webServiceXray.getRequestByAcc(acc);
            mrn = request.getMrn();
            MSH msh = webServiceXray.getMSH();
            File initialFile = new File("/usr/archive_ris/PDF/" + mrn + "_" + acc + ".pdf");
            targetStream = FileUtils.openInputStream(initialFile);
            //File targetFile = new File(msh.getDirectory() + mrn + "_" + acc + ".pdf");
            File targetFile = new File("/home/rispkvic/report/" + mrn + "_" + acc + ".pdf");
            PDFService pdfService = new PDFService();
            pdfService.copyInputStreamToFile(targetStream, targetFile);
            log.info("Copy PDF {} completed!", mrn + "_" + acc + ".pdf");
        } catch (Exception e) {
            log.error("Copy PDF {} error!", mrn + "_" + acc + ".pdf");
            e.printStackTrace();
        }
        return new ResponseEntity<>("PDF Copy Completed!", HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/pdf/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> copyPDFAdmin(@PathVariable String acc) {
        InputStream targetStream = null;
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        String mrn = "";
        try {
            RequestModel request = webServiceXray.getRequestByAcc(acc);
            mrn = request.getMrn();
            MSH msh = webServiceXray.getMSH();
            File initialFile = new File("/usr/archive_ris/PDF/" + mrn + "_" + acc + ".pdf");
            targetStream = FileUtils.openInputStream(initialFile);
            File targetFile = new File(msh.getDirectory().replace("reporttemp", "re_report") + mrn + "_" + acc + ".pdf");
            PDFService pdfService = new PDFService();
            pdfService.copyInputStreamToFile(targetStream, targetFile);
            log.info("Copy PDF {} completed!", mrn + "_" + acc + ".pdf");
        } catch (Exception e) {
            log.error("Copy PDF {} error!", mrn + "_" + acc + ".pdf");
            e.printStackTrace();
        }
        return new ResponseEntity<>("PDF Copy Completed!", HttpStatus.OK);
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
            //System.out.println("after:"+tempDate);
            datetime = newdfRp.format(tempDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datetime;
    }

    private static String convertBirthDate(Date birthdate) {
        String dob = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyyMMdd");
        birthdate  = addHoursToJavaUtilDate(birthdate, 17);
        try {
            dob = newdf.format(birthdate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dob;
    }
    private static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
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

    private static String convertGender(String gender) {
        String gen = null;
        if (gender.equals("MALE")) {
            gen = "M";
        } else if (gender.equals("FEMALE")) {
            gen = "F";
        } else {
            gen = "O";
        }
        return gen;
    }

    private static int position(String conclusion) {
        int position = 0;
        try {
            position = conclusion.indexOf("Clinical Elements:");
            if (position < 0) {
                position = conclusion.indexOf("Major Problem:");
                if (position < 0) {
                    return 10000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return position;
    }

    private static String getCe_Mp(String conclusion) {
        String ce_mp = "";
        String temp = "";
        List<String> ceList = new ArrayList<>();
        try {
            Integer position = conclusion.indexOf("Clinical Elements:");
            if (position >= 0) {
                temp = conclusion.substring(position, conclusion.length());
                String[] list = temp.split("Clinical Elements:");
                for (int i = 0; i < list.length; i++) {
                    ceList.add(list[i]);
                }
                ce_mp = "Clinical Elements:" + ceList.get(1);
            } else {
                Integer position1 = conclusion.indexOf("Major Problem:");
                if (position1 >= 0) {
                    temp = conclusion.substring(position1, conclusion.length());
                    String[] list = temp.split("Major Problem:");
                    for (int i = 0; i < list.length; i++) {
                        ceList.add(list[i]);
                    }
                    ce_mp = "Major Problem:" + ceList.get(1);
                } else {
                    return "";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ce_mp;
    }

    private static List<OBXModel> getOBXList(ReportSegment reportSegment, OBX obx) {
        List<OBXModel> obsList = new ArrayList<>();
        try {
            // Title
            for (int i = 0; i < 4; i++) {
                OBXModel obxModelTitle = new OBXModel();
                if (i == 0) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Date: " + reportSegment.getDateTime());
                } else if (i == 1) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Title: " + reportSegment.getTitle());
                } else if (i == 2) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Providers: " + reportSegment.getProvider());
                } else {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("");
                }
                obsList.add(obxModelTitle);
            }
            // Describe
            String[] temps = reportSegment.getDescribe().split("\n");
            int startDescribe = 4 + temps.length;
            int reasonIndex = 0;
            for (int i = 0; i < temps.length; i++) {
                OBXModel obxModel = new OBXModel();
                obxModel.setIndex(i + 4);
                obxModel.setType("TX");
                obxModel.setObs_iden(reportSegment.getXray_code());
                if (temps[i].replaceAll("[\\t\\n\\r]+", " ").contains("Lý do khảo sát – Indicated reason: ")) {
                    obxModel.setObs_sub("R");
                    reasonIndex = i + 1;
                } else if (reasonIndex == 1) {
                    obxModel.setObs_sub("D");
                    reasonIndex = 2;
                } else {
                    obxModel.setObs_sub("");
                }
                obxModel.setUnits(" " + temps[i].replaceAll("[\\t\\n\\r]+", " "));
                obsList.add(obxModel);
            }
            // conclusion
            String[] temps1 = reportSegment.getConclusion().split("\n");
            int startConclusion = startDescribe + temps1.length;
            for (int i = 0; i < temps1.length; i++) {
                OBXModel obxModel = new OBXModel();
                obxModel.setIndex(i + startDescribe);
                obxModel.setType("TX");
                obxModel.setObs_iden(reportSegment.getXray_code());
                if (i == 0) {
                    obxModel.setObs_sub("C");
                } else {
                    obxModel.setObs_sub("");
                }
                obxModel.setUnits(" " + temps1[i].replaceAll("[\\t\\n\\r]+", " "));
                obsList.add(obxModel);
            }
            // note

            String[] temps2 = reportSegment.getNote().split("\n");
            int startNote = startConclusion + temps2.length + 1;
            for (int i = 0; i < temps2.length; i++) {
                OBXModel obxModel = new OBXModel();
                obxModel.setIndex(i + startConclusion);
                obxModel.setType("TX");
                obxModel.setObs_iden(reportSegment.getXray_code());
                obxModel.setObs_sub("");
                obxModel.setUnits(" " + temps2[i].replaceAll("[\\t\\n\\r]+", " "));
                obsList.add(obxModel);
            }
            // Sign
            int starSign = startNote + 4;
            OBXModel obxModelNote = new OBXModel();
            obxModelNote.setIndex(startNote - 1);
            obxModelNote.setType("TX");
            obxModelNote.setObs_iden(reportSegment.getXray_code());
            obxModelNote.setObs_sub("");
            obxModelNote.setUnits("");
            obsList.add(obxModelNote);
            for (int i = 0; i < 4; i++) {
                OBXModel obxModelSign = new OBXModel();
                if (i == 0 || i == 2) {
                    obxModelSign.setIndex(i + startNote);
                    obxModelSign.setType("TX");
                    obxModelSign.setObs_iden(reportSegment.getXray_code());
                    if (i == 0) {
                        obxModelSign.setObs_sub("ENDCE");
                    } else {
                        obxModelSign.setObs_sub("");
                    }
                    obxModelSign.setUnits("");
                } else if (i == 1) {
                    obxModelSign.setIndex(i + startNote);
                    obxModelSign.setType("TX");
                    obxModelSign.setObs_iden(reportSegment.getXray_code());
                    obxModelSign.setObs_sub("");
                    obxModelSign.setUnits("SIGNED BY ");
                } else {
                    obxModelSign.setIndex(i + startNote);
                    obxModelSign.setType("TX");
                    obxModelSign.setObs_iden(reportSegment.getXray_code());
                    obxModelSign.setObs_sub("");
                    obxModelSign.setUnits(reportSegment.getSign() + " (" + reportSegment.getProvider() + ")");
                }
                obsList.add(obxModelSign);
            }
            // File
            if (!reportSegment.getXray_code().contains("CT")) {
                int starFile = starSign;
                OBXModel obxModelFile = new OBXModel();
                obxModelFile.setIndex(starSign);
                obxModelFile.setType("ST");
                obxModelFile.setObs_iden(obx.getObs_iden());
                obxModelFile.setObs_sub(obx.getObs_sub_id());
                obxModelFile.setObs_val(obx.getObs_value());
                obxModelFile.setUnits(obx.getUnits() + "Â»");
                obsList.add(obxModelFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obsList;
    }

    private static List<OBXModel> getOBXList1(ReportSegment reportSegment, OBX obx) {
        List<OBXModel> obsList = new ArrayList<>();
        boolean statusCE = false;
        try {
            String conclusion = VNCharacterUtils.removeAccent(reportSegment.getConclusion());
            conclusion = Normalizer.normalize(conclusion, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            //conclusion = conclusion.contains("CLINICAL ELEMENTS:") ? conclusion : (conclusion + "\r\nCLINICAL ELEMENTS:\r\n ");
            statusCE = conclusion.contains("CLINICAL ELEMENTS:") ? true : false;
            String[] temps1 = conclusion.split("\n");
            String title = "Null";
            Boolean flags = true;
            try {
                title = temps1[0].replaceAll("[\\t\\n\\r]+", " ");
            } catch (Exception e) {
                flags = false;
                e.printStackTrace();
            }
            // Title
            for (int i = 0; i < 4; i++) {
                OBXModel obxModelTitle = new OBXModel();
                if (i == 0) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Date: " + reportSegment.getDateTime());
                } else if (i == 1) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Title: " + title);
                } else if (i == 2) {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("Providers: " + reportSegment.getProvider());
                } else {
                    obxModelTitle.setIndex(i);
                    obxModelTitle.setType("TX");
                    obxModelTitle.setObs_iden(reportSegment.getXray_code());
                    obxModelTitle.setObs_sub("");
                    obxModelTitle.setUnits("");
                }
                obsList.add(obxModelTitle);
            }

            int startConclusion = flags ? (4 + (temps1.length - 1)) : (4 + temps1.length);
            if (flags) {
                for (int i = 0; i < (temps1.length - 1); i++) {
                    OBXModel obxModel = new OBXModel();
                    obxModel.setIndex(i + 4);
                    obxModel.setType("TX");
                    obxModel.setObs_iden(reportSegment.getXray_code());
                    //obxModel.setObs_sub("");
                    if (i == 0) {
                        obxModel.setObs_sub("C");
                    } else {
                        try {
                            String line =  temps1[i+1];
                            if (line.contains("CLINICAL ELEMENTS:")) {
                                obxModel.setObs_sub("CE");
                            } else {
                                obxModel.setObs_sub("");
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            obxModel.setObs_sub("");
                        }
                    }
                    obxModel.setUnits(temps1[i + 1].replaceAll("[\\t\\n\\r]+", " "));
                    obsList.add(obxModel);
                }
            } else {
                for (int i = 0; i < temps1.length; i++) {
                    OBXModel obxModel = new OBXModel();
                    obxModel.setIndex(i + 4);
                    obxModel.setType("TX");
                    obxModel.setObs_iden(reportSegment.getXray_code());
                    //obxModel.setObs_sub("");
                    if (i == 0) {
                        obxModel.setObs_sub("C");
                    } else {
                        try {
                            String line =  temps1[i+1];
                            if (line.contains("CLINICAL ELEMENTS:")) {
                                obxModel.setObs_sub("CE");
                            } else {
                                obxModel.setObs_sub("");
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            obxModel.setObs_sub("");
                        }
                    }
                    obxModel.setUnits(temps1[i].replaceAll("[\\t\\n\\r]+", " "));
                    obsList.add(obxModel);
                }
            }
            //int startNote = startConclusion;
            for (int i = 0; i < 4; i++) {
                OBXModel obxModelSign = new OBXModel();
                if (i == 0 || i == 2) {
                    obxModelSign.setIndex(i + startConclusion);
                    obxModelSign.setType("TX");
                    obxModelSign.setObs_iden(reportSegment.getXray_code());
                    if (i == 0 && statusCE) {
                        obxModelSign.setObs_sub("ENDCE");
                    } else {
                        obxModelSign.setObs_sub("");
                    }
                    obxModelSign.setUnits("");
                } else if (i == 1) {
                    obxModelSign.setIndex(i + startConclusion);
                    obxModelSign.setType("TX");
                    obxModelSign.setObs_iden(reportSegment.getXray_code());
                    obxModelSign.setObs_sub("");
                    obxModelSign.setUnits("SIGNED BY ");
                } else {
                    obxModelSign.setIndex(i + startConclusion);
                    obxModelSign.setType("TX");
                    obxModelSign.setObs_iden(reportSegment.getXray_code());
                    obxModelSign.setObs_sub("");
                    obxModelSign.setUnits(reportSegment.getSign() + " (" + reportSegment.getRadiogist() + ")");
                }
                obsList.add(obxModelSign);
            }
            int starSign = startConclusion + 4;
            OBXModel obxModelFile = new OBXModel();
            obxModelFile.setIndex(starSign);
            obxModelFile.setType("ST");
            obxModelFile.setObs_iden(obx.getObs_iden());
            obxModelFile.setObs_sub(obx.getObs_sub_id());
            obxModelFile.setObs_val(obx.getObs_value());
            obxModelFile.setUnits(obx.getUnits() + "»");
            obsList.add(obxModelFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obsList;
    }


    private static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }


    public List<String> getHL7Temps(String dir) {
        List<String> hl7s = null;
        try {
            Set<String> s = listFilesUsingJavaIO(dir);
            int n = s.size();
            hl7s = new ArrayList<String>(n);
            for (String x : s)
                hl7s.add(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hl7s;
    }



    public static Boolean checkBefore(Date date1, Date date2) {
        try {
            if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static String add5Minute(String date) {
        int addMinuteTime = 5;
        String dateTimeAdd = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String hour = date.substring(8, 10);
        String minute = date.substring(10, 12);
        String second = date.substring(12, 14);
        String inputDateInString= year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        try {
            Date targetTime = sdf1.parse(inputDateInString);
            targetTime = DateUtils.addMinutes(targetTime, addMinuteTime);
            dateTimeAdd = sdf.format(targetTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTimeAdd;
    }
    private static Boolean validateReport(String filePath) {
        Boolean check = true;
        List<String> stringList = new ArrayList<>();
        try {
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
            String acc = fileName.substring(0, fileName.indexOf("_"));
            List<String> lines = FileUtils.readLines(new File(filePath));
            int count = 0;
            int countSign = 0;
            int countFile= 0;
            int countPDF = 0;
            int countProvider = 0;
            for (String line: lines) {
                if(line != null) {
                    if (line.toLowerCase().indexOf(acc.toLowerCase()) != -1 ) {
                        String [] keywords = line.split(acc);
                        count +=(keywords.length - 1);
                    }
                    if (line.toLowerCase().indexOf("SIGNED BY".toLowerCase()) != -1 ) {
                        String [] keywords = line.split("SIGNED BY");
                        countSign +=(keywords.length - 1);
                    }
                    if (line.toLowerCase().indexOf("File Location".toLowerCase()) != -1 ) {
                        String [] keywords = line.split("File Location");
                        countFile +=(keywords.length - 1);
                    }
                    if (line.toLowerCase().indexOf(".pdf".toLowerCase()) != -1 ) {
                        String [] keywords = line.split(".pdf");
                        countPDF +=(keywords.length - 1);
                    }

                    if (line.toLowerCase().indexOf("Providers: ".toLowerCase()) != -1 ) {
                        String [] keywords = line.split("Providers: ");
                        countProvider +=(keywords.length - 1);
                    }
                    if(line.equals("null")) {
                        return false;
                    }

                    stringList.add(line.substring(0, line.indexOf("|")));
                }
            }
            Set<String> store = new HashSet<>();
            for (String name : stringList) {
                if (store.add(name) == false) {
                    if(!name.equals("OBX")) {
                        check = false;
                    }
                }
            }

            if(count < 7) {
                check = false;
            }
            if(countSign > 1) {
                check = false;
            }
            if(countFile > 1) {
                check = false;
            }
            if(countPDF > 1) {
                check = false;
            }

            if(countProvider > 1) {
                check = false;
            }
            return check;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void publicReport(String fileName) {
        try {
            String dir = fileName.substring(0, fileName.lastIndexOf("reporttemp")+10);
            String file_copy = fileName.substring(fileName.lastIndexOf("reporttemp")+11, fileName.length());
            String file_name = fileName.substring(fileName.lastIndexOf("reporttemp")+11, fileName.lastIndexOf(".hl7"));
            String datetime = file_name.split("_")[1];
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            String day = datetime.substring(6, 8);
            String directory = dir.replace("reporttemp", "hl7_report_backup") + "/" + year
                    + "/" + month + "/" + day;
            String dirPublic = dir.replace("reporttemp", "report");
            createDirectory(directory);
            File initialFile = new File(fileName);
            InputStream targetStream = FileUtils.openInputStream(initialFile);
            File targetFile = new File(directory + "/" +file_copy);
            copyInputStreamToFile(targetStream, targetFile);
            log.info("Copy report to Backup!");
            moveFile(fileName, dirPublic+"/"+file_name+".hl7");
            log.info("Move report to Public!");
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Cant copy backup and public report file!");
        }
    }

    private static void tempReportError(String fileName) {
        try {
            String originPath = fileName;
            String dir = fileName.substring(0, fileName.lastIndexOf("reporttemp")+10);
            String file_name = fileName.substring(fileName.lastIndexOf("reporttemp")+11, fileName.lastIndexOf(".hl7"));
            String datetime = file_name.split("_")[1];
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            String day = datetime.substring(6, 8);
            String directory = dir.replace("reporttemp", "reporttemp_backup") + "/" + year
                    + "/" + month + "/" + day;
            createDirectory(directory);
            moveFile(originPath, directory+"/"+file_name+".hl7");
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Cant copy backup and public report file!");
        }
    }

    private static void createDirectory(String dir) {
        try {
            Path path = Paths.get(dir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Create directory {} success!", dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to create directory!" + e.getMessage());
        }
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
            inputStream.close();
        }
    }

    private static void moveFile(String source, String destination){
        try {
            Path temp = Files.move
                    (Paths.get(source),
                            Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);

            if (temp != null) {
                System.out.println("File renamed and moved successfully");
            } else {
                System.out.println("Failed to move the file");
            }
        } catch (Exception e){
            log.error("Move file {} error!");
            e.printStackTrace();
        }
    }

    private static String CreateProcessHL7(String uri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Data attached to the request.
        HttpEntity<String> requestBody = new HttpEntity<>("{}",headers);

        // Send request with POST method.
        ResponseEntity<String> result
                = restTemplate.postForEntity(uri, requestBody, String.class);
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            String r = result.getBody();
            log.info("(Client Side) Process Created.");
            return r;
        }
        return null;
    }

    public static void main(String[] args)  {
        /*String text = "KẾT QUẢ X-QUANG (XRAY REPORT)\n" +
                " Lý do khảo sát – Indicated reason: \n" +
                " \n" +
                " NGỰC THẲNG – CHEST AP\n" +
                " NGỰC NGHIÊNG – CHEST LATERAL\n" +
                " ĐỈNH ƯỠN – CHEST LORDOTIC\n" +
                " Mô tả – Finddings:          \n" +
                " Bóng tim không to\n" +
                " Không thấy tổn thương tiến triển trên hai phế trường\n" +
                " Không hạch trung thất.\n" +
                " Động mạch chủ ngực và phổi trong giới hạn bình thường\n" +
                " Màng phổi hai bên chưa thấy bất thường.\n" +
                " Thành ngực bình thường\n" +
                " Vòm hoành hai bên trong giới hạn bình thường\n" +
                " Normal cardiac silhouette\n" +
                " There is no lesion in the pulmonary parenchyma, \n" +
                " No hilar or mediastinal mass is seen\n" +
                " The thoracic aorta and pulmonary arteries are normal in diameter and shape\n" +
                " There is no pleural effusion\n" +
                " The thoracic walls are normal\n" +
                " Normal diaphragm both sides";
        //System.out.println(getOBXList("US011", text, "").size());
        //Date date = new Date();
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd");
        //date = newdf.parse("2023-02-10 09:00:00");
        //System.out.println(add5Minute("2023-02-10 09:00:00"));
        Date date = new Date();
        date = newdf.parse("2004-06-27");*/
        //System.out.println("dob:"+convertBirthDate(date));
        System.out.println(validateReport("D:/Disk_D/Workspace Victoria/report/3723262_20230526162841_RE.hl7"));
    }
}
