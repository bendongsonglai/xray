package inext.ris.order.xray.export;

import inext.ris.order.xray.code.CodeModel;
import inext.ris.order.xray.hl7.*;
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
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ExportReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportReportService.class);
    @Async
    public void exportHL7(String acc) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        HL7ReportModel hl7 = new HL7ReportModel();
        try {
            MSH msh = webServiceXray.getMSH();
            hl7.setDirectory(msh.getDirectory());
            //hl7.setDirectory("E:\\WORK\\workspace victoria\\bkris\\report\\");
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
            hl7.setLocation("KHOA NOI ");
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
            hl7.setReason(request.getNote() == null ? " " : unaccent(request.getNote()));
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
            reportSegment.setConclusion(conclusion);

            reportSegment.setNote(note != null ? note : "\n");

            OBX obx = webServiceXray.getOBX();
            obx.setUnits(obx.getUnits() + mrn + "_" + acc + ".pdf");
            hl7.setObxList(getOBXList1(reportSegment, obx));
            HL7ServiceReport hl7Service = new HL7ServiceReport();
            hl7Service.createReportHL7(hl7);
            LOGGER.info("Write report HL7 {} completed!", acc);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Write report HL7 {} Error!", acc);
        }
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

    public static String unaccent(String src) {
        try {
            return Normalizer
                    .normalize(src, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }

    }
}
