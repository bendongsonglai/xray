package inext.ris.order.xray.report;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.aes.AES;
import inext.ris.order.xray.clinicalElement.ClinicalElement;
import inext.ris.order.xray.code.CodeModel;
import inext.ris.order.xray.department.DepartmentModel;
import inext.ris.order.xray.doituong.DoituongModel;
import inext.ris.order.xray.hislog.HisLog;
import inext.ris.order.xray.hsm.*;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.majorProblem.MajorProblem;
import inext.ris.order.xray.patient_diagnosis.PatientDiagnosis;
import inext.ris.order.xray.patient_info.PatientInfoModel;
import inext.ris.order.xray.qr.QRCode;
import inext.ris.order.xray.referrer.ReferrerModel;
import inext.ris.order.xray.reportsigned.Signature;
import inext.ris.order.xray.request.RequestModel;
import inext.ris.order.xray.request_detail.RequestDetailModel;
import inext.ris.order.xray.signature.SignatureAccount;
import inext.ris.order.xray.user.UserModel;
import inext.ris.order.xray.workstation.WSModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/report")
@Slf4j
@RequiredArgsConstructor
public class ReportAPI {
    @Autowired
    ReportService reportService;
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    @GetMapping
    public ResponseEntity<List<ReportModel>> findAll() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ReportModel reportModel) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportService.existsByAccession(reportModel.getAccession()).compareTo(bi);
        if (res > 0) {
            log.error("Report Model " + reportModel.getAccession() + " existed");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        reportModel.setDictate_date(new Date());
        reportModel.setDictate_time(new Date());
        reportModel.setApprove_date(new Date());
        reportModel.setApprove_time(new Date());
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        AES aes = webServiceXray.getAes();
        try {
            QRCode qrCode = new QRCode();
            qrCode.build(aes.getSecret(), "/var/www/html/QR/", reportModel.getAccession(), aes.getContent());
            log.info("Create QR acc {} created Completed.", reportModel.getAccession());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create QRerror!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reportService.save(reportModel));
    }

    @RequestMapping(value = "/diagnosis/{acc}", method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable String acc) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        try {
            ReportPublic reportPublic = webServiceXray.getReportDetails(acc);
            //String describe = reportPublic.getDescribe();
            String conclusion = reportPublic.getConclusion();
            try {
                List<CE> ces = extractCE(conclusion, webClientConfig);
                List<Integer> mps = extractMP(conclusion, webClientConfig);
                if (mps.size() == 0) {
                    mps.add(1);
                }
                if (ces.size() > 0) {
                    for (CE ce : ces
                    ) {
                        PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
                        patientDiagnosis.setId((long) 0);
                        patientDiagnosis.setMrn(reportPublic.getPatientID());
                        patientDiagnosis.setAccession(acc);
                        patientDiagnosis.setCe(ce.getIndex());
                        patientDiagnosis.setCe_val(ce.getValue());
                        patientDiagnosis.setMp(mps.get(0));
                        patientDiagnosis.setDate(new Date());
                        patientDiagnosis.setEnabled(0);
                        webServiceXray.createPatientDiagnosis(patientDiagnosis);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error diagnosis!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Completed diagnosis!", HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{acc}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateApprove(@PathVariable String acc, @Valid @RequestBody ReportModel reportModel) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Report Model " + reportModel.getAccession() + " existed");
            return new ResponseEntity("Not found!", HttpStatus.NOT_FOUND);
        }
        try {
            reportService.updateReportByAccession(reportModel.getDictate_by(), reportModel.getApprove_by()
                    , reportModel.getDescribereport(), reportModel.getConclusion(), new Date(), reportModel.getApprove_time(), acc);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }


    @RequestMapping(value = "/delete/{bundle}/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteApprove(@PathVariable String bundle, @PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Report Model " + acc + " existed");
            return new ResponseEntity("Not found!", HttpStatus.NOT_FOUND);
        }
        try {
            reportService.deleteReportByAccession(acc+"_"+getTimeDigit(), acc);
            WebClientConfig webClientConfig = new WebClientConfig();
            WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
            HisLog hisLog = new HisLog();
            hisLog.setId((long) 0);
            hisLog.setBundle_id(bundle);
            hisLog.setAccession(acc);
            hisLog.setCode("0");
            hisLog.setMessage("HIS DELETE REPORT SUCCESS");
            hisLog.setStatus("DELETE");
            hisLog.setCreated_time(new Date());
            webServiceXray.createHISLogs(hisLog);
            try {
                List<String> bunles = webServiceXray.getListBundleIDReport(acc);
                for (String bundleid: bunles
                     ) {
                    if(bundleid != bundle) {
                        HisLog hisLog1 = new HisLog();
                        hisLog1.setId((long) 0);
                        hisLog1.setBundle_id(bundleid);
                        hisLog1.setAccession(acc);
                        hisLog1.setCode("0");
                        hisLog1.setMessage("HIS DELETE REPORT SUCCESS");
                        hisLog1.setStatus("DELETE");
                        hisLog1.setCreated_time(new Date());
                        webServiceXray.createHISLogs(hisLog1);
                    }
                }
                log.info("Delete report bundleID #!");
            }catch (Exception e) {
                e.printStackTrace();
                log.info("Error delete report bundleID #!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Delete report accession " + acc +" Success!", HttpStatus.OK);
    }


    @RequestMapping(value = "/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> create(@PathVariable String acc, @RequestBody Report report) {
        String url = report.getUri();
        String user = report.getUser();
        String password = report.getPassword();
        String content = report.getContent();
        String dir = report.getDirectorySync();
        String authStr = user + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(content);
        String decodedString = new String(decodedBytes);
        try {
            URL urlBundle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
            String token = "Basic " + new String(base64Creds);
            // Set timeout as per needs
            connection.setRequestProperty("Authorization", token);
            //System.out.println(token);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            // Set DoOutput to true if you want to use URLConnection for output.
            // Default is false
            connection.setDoOutput(true);

            connection.setUseCaches(true);
            connection.setRequestMethod("POST");

            // Set Headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Write XML
            OutputStream outputStream = connection.getOutputStream();
            byte[] b = decodedString.getBytes("UTF-8");
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();

            // Read XML
            InputStream inputStream = connection.getInputStream();
            byte[] res = new byte[2048];
            int i = 0;
            StringBuilder response = new StringBuilder();
            while ((i = inputStream.read(res)) != -1) {
                response.append(new String(res, 0, i));
            }
            inputStream.close();
            log.info("Created Bundle Report acc {} Completed!", acc);
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            log.error("Error Create Bundle Report {}", acc);
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/sync/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> createAndSync(@PathVariable String acc, @RequestBody Report report) {
        String url = report.getUri();
        String user = report.getUser();
        String password = report.getPassword();
        String content = report.getContent();
        String dir = report.getDirectorySync();
        String secretkey = report.getSecretkey();
        String qrhome = report.getQrhome();
        String linkQR = report.getLinkQR();
        String authStr = user + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(content);
        String decodedString = new String(decodedBytes);
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(decodedString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String input = json.toJSONString();
        log.info("json report {}", input);
        try {
            URL urlBundle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
            String token = "Basic " + new String(base64Creds);
            // Set timeout as per needs
            connection.setRequestProperty("Authorization", token);
            //System.out.println(token);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            // Set DoOutput to true if you want to use URLConnection for output.
            // Default is false
            connection.setDoOutput(true);

            connection.setUseCaches(true);
            connection.setRequestMethod("POST");

            // Set Headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Write XML
            OutputStream outputStream = connection.getOutputStream();
            byte[] b = input.getBytes("UTF-8");
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();

            // Read XML
            InputStream inputStream = connection.getInputStream();
            byte[] res = new byte[2048];
            int i = 0;
            StringBuilder response = new StringBuilder();
            while ((i = inputStream.read(res)) != -1) {
                response.append(new String(res, 0, i));
            }
            inputStream.close();
            int code = connection.getResponseCode();
            log.info("Created Bundle Report acc {} Completed!", acc);
            String curr = getCurrentDate();
            String year = curr.substring(0, 4);
            String month = curr.substring(4, 6);
            String day = curr.substring(6, 8);
            String backup_dir = dir + year + "/" + month + "/" + day + "/";
            createDirectory(backup_dir);
            if (code == 200 || code == 201) {
                String responseReport = getBundleByIdentifer(url + "?identifier=" + acc + "&_sort=_lastUpdated", user, password);
                StringBuilder sbReport = new StringBuilder(responseReport);
                copyInputStreamToFile(backup_dir, sbReport, acc);
                String bundleID = getBundleID(responseReport);
                String reportByID = getBundleByID(url + "/" + bundleID + "?_format=json", user, password);
                StringBuilder hisReport = new StringBuilder(reportByID);
                String report_ris = dir.replace("report", "report-ris");
                copyReportRIS2HIS(report_ris, hisReport, acc);
                try {
                    QRCode qrCode = new QRCode();
                    qrCode.build(secretkey, qrhome, acc, linkQR);
                    log.info("Create QR acc {} created Completed.", acc);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Create QRerror!");
                }
            }
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            log.error("Error Create Bundle Report {}", acc);
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/syncsign/{userid}/{mrn}/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> createAndSyncSign(@PathVariable String userid, @PathVariable String mrn, @PathVariable String acc, @RequestBody Report report) {
        String dirRootPDF = "/usr/archive_ris/PDF";
        String filename = mrn + "_" + acc + ".pdf";
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        HSMModel hsm;
        String hsm_host = null;
        String hsm_port = null;
        String hsm_url = null;
        String hsm_auth = null;
        String hsm_xml = null;
        String hsm_json = null;
        String hsm_pdf = null;
        String hsm_validation = null;
        String accessToken = null;
        try {
            hsm = webServiceXray.getHSM();
            hsm_host = hsm.getHost();
            hsm_port = hsm.getPort();
            hsm_url = hsm.getUrl();
            hsm_auth = hsm.getAuth();
            hsm_xml = hsm.getXml();
            hsm_json = hsm.getJson();
            hsm_pdf = hsm.getPdf();
            hsm_validation = hsm.getValidation();
            SignatureAccount signatureAccount = webServiceXray.getSignatureAccount(userid);
            Authenticate authenticate = new Authenticate();
            authenticate.setUsername(signatureAccount.getUser_hsm());
            authenticate.setPassword(signatureAccount.getPass_hsm());
            authenticate.setRememberMe(false);
            accessToken = authenticate(hsm_url + hsm_auth, authenticate);
            Image image = getImage(hsm_url + "/api/certificate/getImage?serial=" + signatureAccount.getCts()
                    + "&pin=" + signatureAccount.getPin(), accessToken);
            PDFSignPositionData pdfSignData = new PDFSignPositionData();
            List<SigningRequestContentPDF> signingRequestContents = new ArrayList<>();
            SigningRequestContentPDF signingRequestContent = new SigningRequestContentPDF();
            signingRequestContent.setDocumentName(filename);
            File filePDF = new File(dirRootPDF + "/report/" + filename);
            String pdfHASH = encodeFileToBase64(filePDF);
            signingRequestContent.setData(pdfHASH);
            Location location = new Location();
            location.setVisibleX(345);
            location.setVisibleY(55);
            location.setVisibleWidth(266);
            location.setVisibleHeight(80);
            signingRequestContent.setLocation(location);
            ExtraInfo extraInfo = new ExtraInfo();
            extraInfo.setPageNum(1);
            signingRequestContent.setExtraInfo(extraInfo);
            signingRequestContent.setImageSignature(image.getData());
            signingRequestContents.add(signingRequestContent);
            pdfSignData.setSigningRequestContents(signingRequestContents);
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setPin(signatureAccount.getPin());
            tokenInfo.setSerial(signatureAccount.getCts());
            pdfSignData.setTokenInfo(tokenInfo);
            ResponseSignedPDF responseSignedPDF = positionPdf(hsm_url + hsm_pdf, accessToken, pdfSignData);
            log.info("Status HSM:"+responseSignedPDF.getStatus());
            if (responseSignedPDF.getStatus() == 0) {
                base64ToFile(dirRootPDF + "/reportsigned/" + filename, responseSignedPDF.getData());
                File filePDFSigned = new File(dirRootPDF + "/reportsigned/" + filename);
                String pdfHASHSigned = encodeFileToBase64(filePDFSigned);
                String url = report.getUri();
                String user = report.getUser();
                String password = report.getPassword();
                String content = report.getContent();
                String dir = report.getDirectorySync();
                String secretkey = report.getSecretkey();
                String qrhome = report.getQrhome();
                String linkQR = report.getLinkQR();
                String authStr = user + ":" + password;
                String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(content);
                String decodedString = new String(decodedBytes);
                try {
                    Signature signature = new Signature();
                    signature.setId((long) 0);
                    signature.setAccession(acc);
                    signature.setSignature(dirRootPDF + "/reportsigned/" + filename);
                    signature.setCreated_time(new Date());
                    webServiceXray.createReportSignedPath(signature);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("Create Report Signed Path Error!");
                }
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(decodedString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String input = json.toJSONString();
                String newInput = input.replace("\"contentType\":\"application\\/xml\"", "\"contentType\":\"application\\/pdf\", \"data\":\""+pdfHASHSigned+"\"");

                log.info("json report signed {}", newInput);
                try {
                    URL urlBundle = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
                    String token = "Basic " + new String(base64Creds);
                    // Set timeout as per needs
                    connection.setRequestProperty("Authorization", token);
                    //System.out.println(token);
                    connection.setConnectTimeout(20000);
                    connection.setReadTimeout(20000);
                    // Set DoOutput to true if you want to use URLConnection for output.
                    connection.setDoOutput(true);
                    connection.setUseCaches(true);
                    connection.setRequestMethod("POST");
                    // Set Headers
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Content-Type", "application/json");
                    // Write XML
                    OutputStream outputStream = connection.getOutputStream();
                    byte[] b = newInput.getBytes("UTF-8");
                    outputStream.write(b);
                    outputStream.flush();
                    outputStream.close();
                    // Read XML
                    InputStream inputStream = connection.getInputStream();
                    byte[] res = new byte[2048];
                    int i = 0;
                    StringBuilder response = new StringBuilder();
                    while ((i = inputStream.read(res)) != -1) {
                        response.append(new String(res, 0, i));
                    }
                    inputStream.close();
                    int code = connection.getResponseCode();
                    log.info("Created Bundle Report acc {} Completed!", acc);
                    String curr = getCurrentDate();
                    String year = curr.substring(0, 4);
                    String month = curr.substring(4, 6);
                    String day = curr.substring(6, 8);
                    String backup_dir = dir + year + "/" + month + "/" + day + "/";
                    createDirectory(backup_dir);
                    if (code == 200 || code == 201) {
                        String responseReport = getBundleByIdentifer(url + "?identifier=" + acc + "&_sort=_lastUpdated", user, password);
                        StringBuilder sbReport = new StringBuilder(responseReport);
                        copyInputStreamToFile(backup_dir, sbReport, acc);
                        String bundleID = getBundleID(responseReport);
                        String reportByID = getBundleByID(url + "/" + bundleID + "?_format=json", user, password);
                        StringBuilder hisReport = new StringBuilder(reportByID);
                        String report_ris = dir.replace("report", "report-ris");
                        copyReportRIS2HIS(report_ris, hisReport, acc);
                        try {
                            QRCode qrCode = new QRCode();
                            qrCode.build(secretkey, qrhome, acc, linkQR);
                            log.info("Create QR acc {} created Completed.", acc);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("Create QR error!");
                        }
                    }
                    return ResponseEntity.ok(response.toString());
                } catch (Exception e) {
                    log.error("Error Create Bundle Report {}", acc);
                    e.printStackTrace();
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return new ResponseEntity<>("Signature acc " + acc + " Error!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error Create Bundle Report {}", acc);
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/sendHIS/{userris}/{acc}/{approvetime}", method = RequestMethod.POST)
    public ResponseEntity<String> sendHIS(@PathVariable String userris, @PathVariable String acc, @PathVariable String approvetime
            , @RequestBody HISInfo hisInfo) {
        String year = approvetime.substring(0, 4);
        String month = approvetime.substring(4, 6);
        String day = approvetime.substring(6, 8);
        String backup_dir = "/home/" + userris + "/report/" + year + "/" + month + "/" + day + "/";
        String dir_report = backup_dir + acc + ".json";
        String content = readFileReport(dir_report);
        String curr = getCurrentDate();
        String year1 = curr.substring(0, 4);
        String month1 = curr.substring(4, 6);
        String day1 = curr.substring(6, 8);
        String response_dir = "/home/" + userris + "/his/" + year1 + "/" + month1 + "/" + day1 + "/";
        createDirectory(response_dir);
        try {
            String response = sendReportBunble(hisInfo.getUrl(), content, acc, hisInfo.getUser(), hisInfo.getPassword());
            StringBuilder sbHIS = new StringBuilder(response);
            copyResponseHISToFile(response_dir, sbHIS, acc);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Send HIS Failed!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Send HIS Success!", HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteHIS/{userris}/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteHIS(@PathVariable String userris, @PathVariable String acc
            , @RequestBody HISInfo hisInfo) {
        String content = "{\"accessionNumber\": '" + acc + "'}";
        String curr = getCurrentDate();
        String year1 = curr.substring(0, 4);
        String month1 = curr.substring(4, 6);
        String day1 = curr.substring(6, 8);
        String response_dir = "/home/" + userris + "/his/" + year1 + "/" + month1 + "/" + day1 + "/";
        createDirectory(response_dir);
        try {
            String response = deleteReportHIS(hisInfo.getUrl(), content, acc, hisInfo.getUser(), hisInfo.getPassword());
            StringBuilder sbHIS = new StringBuilder(response);
            copyResponseHISToFile(response_dir, sbHIS, acc);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Send HIS Failed!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Send HIS Success!", HttpStatus.OK);
    }

    @RequestMapping(value = "/qr/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> createQR(@PathVariable String acc, @RequestBody Report report) {
        String secretkey = report.getSecretkey();
        String qrhome = report.getQrhome();
        String linkQR = report.getLinkQR();
        try {
            QRCode qrCode = new QRCode();
            qrCode.build(secretkey, qrhome, acc, linkQR);
            log.info("Create QR acc {} created Completed.", acc);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create QRerror!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/qr/mrn/{mrn}/{acc}", method = RequestMethod.POST)
    public ResponseEntity<String> createQR_MRN(@PathVariable String mrn, @PathVariable String acc, @RequestBody Report report) {
        String secretkey = report.getSecretkey();
        String qrhome = report.getQrhome();
        String linkQR = report.getLinkQR();
        try {
            QRCode qrCode = new QRCode();
            qrCode.buildWithMrn(secretkey, qrhome, mrn, acc, linkQR);
            log.info("Create QR acc {} created Completed.", acc);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create QRerror!");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/receptionist/mrn/{mrn}", method = RequestMethod.POST)
    public ResponseEntity<String> receptionist(@PathVariable String mrn, @RequestBody Report report) {
        String secretkey = report.getSecretkey();
        String qrhome = report.getQrhome();
        String linkQR = report.getLinkQR();
        try {
            QRCode qrCode = new QRCode();
            qrCode.buildWithMRNDES(secretkey, qrhome, mrn, linkQR);
            log.info("Create QR mrn {} created Completed.", mrn);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Create QR error!");
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(linkQR + inext.ris.order.xray.qr.AES.encrypt(mrn, secretkey), HttpStatus.OK);
    }

    @RequestMapping(value = "/idivi/save", method = RequestMethod.POST)
    public ResponseEntity<ReportPublic> createReportByAcc(@RequestBody iDiVi idivi) {
        ReportPublic reportPublic = new ReportPublic();
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        ReportModel reportModel = new ReportModel();
        RequestDetailModel requestDetailModel;
        WSModel wsModel;
        DoituongModel doituongModel;
        try {
            UserModel userModel = webServiceXray.getUserByLogin(idivi.getUser());
            requestDetailModel = webServiceXray.getRequestDetailsByAcc(idivi.getAccession());
            requestDetailModel.setAuto_save_time(convertDatetime(idivi.getDateStart()));
            requestDetailModel.setApproved_time(convertDatetime(idivi.getDateStart()));
            requestDetailModel.setStart_time(getDatetime());
            requestDetailModel.setAssign_time(getDatetime());
            requestDetailModel.setAssign(userModel.getCode());
            requestDetailModel.setAssign_by(userModel.getId().toString());
            requestDetailModel.setReport_status("1");
            requestDetailModel.setStatus("APPROVED");
            requestDetailModel.setPage("END");
            requestDetailModel.setTemp_report(idivi.getConclusion());
            requestDetailModel.setReport_book(getDatetime());
            webServiceXray.updateRequestDetail(requestDetailModel);

            reportModel.setId((long) 0);
            reportModel.setAccession(idivi.getAccession());
            reportModel.setDescribereport(idivi.getDescribe());
            reportModel.setConclusion(idivi.getConclusion());
            reportModel.setNote(idivi.getSuggestion());
            reportModel.setBirad(null);
            reportModel.setHistory(null);
            reportModel.setCalcium(null);
            reportModel.setCoronary(null);
            reportModel.setKey_image_link(null);
            reportModel.setDictate_by(userModel.getId().toString());
            reportModel.setDictate_date(getDatetime());
            reportModel.setDictate_time(getDatetime());
            reportModel.setApprove_by(userModel.getId().toString());
            reportModel.setApprove_date(getDatetime());
            reportModel.setDictate_time(getDatetime());
            reportModel.setStatus(null);
            reportModel.setTechnical(requestDetailModel.getGhichu());
            reportModel.setWorkstation(null);
            reportModel.setDoituong(null);
            reportService.save(reportModel);
            /* start report to JSON*/
            ReportToJson reportToJson = new ReportToJson();
            RequestModel requestModel = null;
            PatientInfoModel patientInfoModel = null;
            ReferrerModel referrerModel = null;
            DepartmentModel departmentModel = null;
            CodeModel codeModel = null;
            try {
                requestModel = webServiceXray.getRequestByAcc(requestDetailModel.getAccession());
                patientInfoModel = webServiceXray.getPatientByMrn(requestModel.getMrn());
                referrerModel = webServiceXray.getRefererrerByID(Long.valueOf(requestModel.getReferrer()));
                departmentModel = webServiceXray.getDepartmentBydepartID(requestModel.getDepartment_id());
                codeModel = webServiceXray.getXrayCodeByCode(requestDetailModel.getXray_code());
            } catch (Exception e) {
                log.error("Error report JSON body!");
                e.printStackTrace();
            }
				/*String bodyjson = reportToJson.bodyReport(patientInfoModel, requestModel, codeModel, departmentModel, referrerModel, requestDetailModel, reportModel, userModel);
				log.info("Report JSON: "+bodyjson);
				Hapi hapi = webServiceXray.getHapi();
				String url = "http://"+hapi.getIp()+":"+hapi.getPort()+hapi.getPath()+"Bundle";
				System.out.println("url: "+url);
				String  reportReponse = sendReportBunble(url, bodyjson, idivi.getAccession(), hapi.getUser(), hapi.getPass());
				if(!reportReponse.equals(null)) {
					AES aes = webServiceXray.getAes();
					try {
						QRCode qrCode = new QRCode();
						qrCode.build(aes.getSecret(), "/var/www/html/QR/", idivi.getAccession(), aes.getContent());
						log.info("Create QR acc {} created Completed.", idivi.getAccession());
					} catch (Exception e) {
						e.printStackTrace();
						log.error("Create QRerror!");
						return ResponseEntity.badRequest().build();
					}*/
            reportPublic.setPatientID(patientInfoModel.getMrn());
            reportPublic.setPatientName(patientInfoModel.getName());
            reportPublic.setGender(patientInfoModel.getSex().equals("M") ? "Nam" : "Nữ");
            reportPublic.setDob(convertDateOfBirth(patientInfoModel.getBirth_Date()));
            reportPublic.setAddress(patientInfoModel.getAddress());
            reportPublic.setModality(codeModel.getXray_type_code());
            reportPublic.setAccession(idivi.getAccession());
            reportPublic.setDepartment(departmentModel.getName_vie());
            reportPublic.setRefererrer(referrerModel.getName() + " " + referrerModel.getLastname());
            reportPublic.setPerformerCode(userModel.getLogin());
            reportPublic.setPerformerCchn(userModel.getDf_code());
            reportPublic.setPerformerName(userModel.getName());
            UserModel userModel1 = webServiceXray.getUserByID((long) 3);
            reportPublic.setTechnical(userModel1.getName());
            //wsModel = webServiceXray.getWorkstation(requestDetailModel.getWorkstation());
            //doituongModel = webServiceXray.getDoituong(requestDetailModel.getDoituong());
            doituongModel = null;
			/*reportPublic.setWorkstation(null);
            if (departmentModel != null) {
                reportPublic.setDoituong(doituongModel.getName());
            }*/
            reportPublic.setNote(requestDetailModel.getGhichu());
            reportPublic.setReason(requestModel.getNote());
            reportPublic.setProcedure(codeModel.getDescription());
            reportPublic.setDatetime(requestDetailModel.getAssign_time().toString());
            reportPublic.setDescribe(idivi.getDescribe());
            reportPublic.setConclusion(idivi.getConclusion());
            reportPublic.setSuggestion(idivi.getSuggestion());
            reportPublic.setDataQR(encodeFileToBase64Binary("/var/www/html/QR/" + idivi.getAccession() + ".png"));
            log.info("Report acc {} save success!", idivi.getAccession());
				/*} else {
					log.error("Create report error!");
					return ResponseEntity.badRequest().build();
				}*/

        } catch (
                Exception e) {
            e.printStackTrace();
            log.error("Create report error!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reportPublic);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkReportByAcc(@RequestParam(required = false) String acc, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP check report {}", ip);
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportService.existsByAccession(acc).compareTo(bi);
        if (res > 0) {
            log.info("Check Report with ACCESSION " + acc + " does existed");
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }



    @GetMapping(value = "/signed/stream/{mrn}/{acc}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getFileSigned(@PathVariable String mrn, @PathVariable String acc) throws Exception {
        InputStream is = null;
        try {
            File initialFile = new File("/usr/archive_ris/PDF/reportsigned/"+mrn + "_" + acc +".pdf");
            is = new FileInputStream(initialFile);
            byte[] bytes = StreamUtils.copyToByteArray(is);
            return ResponseEntity
                    .ok()
                    .header("Content-Disposition", "inline; filename=\"" + mrn + "_" + acc +".pdf" + "\"")
                    .contentType(
                            MediaType.parseMediaType("application/pdf"))
                    .contentLength(bytes.length)
                    .body(bytes);
        } catch (final Exception e){
            log.error("Unable to read and write the pdf from " + "serverURL", e);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("application/octet-stream"))
                    .body(null);
        }
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ResponseEntity<ReportPublic> getReportByAcc(@RequestParam(required = false) String acc, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP get report {}", ip);
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        ReportPublic reportPublic = new ReportPublic();
        UserModel userModel;
        RequestModel requestModel = null;
        RequestDetailModel requestDetailModel;
        PatientInfoModel patientInfoModel;
        ReferrerModel referrerModel = null;
        DepartmentModel departmentModel;
        CodeModel codeModel;
        WSModel wsModel;
        DoituongModel doituongModel;
        try {
            Boolean checkReport = false;
            BigInteger bi = new BigInteger("0");
            int res;
            res = reportService.existsByAccession(acc).compareTo(bi);
            if (res > 0) {
                checkReport = true;
            }
            if (checkReport) {
                ReportModel report = reportService.getReportByAcc(acc);
                if (!report.getApprove_by().equals("null")) {
                    userModel = webServiceXray.getUserByID(Long.valueOf(report.getApprove_by()));
                } else {
                    userModel = webServiceXray.getUserByID(Long.valueOf("2"));
                }
                UserModel userModel1 = webServiceXray.getUserByID((long) 3);
                requestModel = webServiceXray.getRequestByAcc(acc);
                requestDetailModel = webServiceXray.getRequestDetailsByAcc(acc);
                patientInfoModel = requestModel.getId() == null ? null : webServiceXray.getPatientByMrn(requestModel.getMrn());
                try {
                    referrerModel = webServiceXray.getRefererrerByRefID(requestModel.getReferrer());
                } catch (Exception e) {
                    e.printStackTrace();
                    referrerModel = webServiceXray.getRefererrerByID(Long.valueOf(requestModel.getId() == null ? "1" : requestModel.getReferrer()));
                }
                departmentModel = webServiceXray.getDepartmentBydepartID(requestModel.getId() == null ? "001" : requestModel.getDepartment_id());
                codeModel = requestDetailModel.getId() == null ? null : webServiceXray.getXrayCodeByCode(requestDetailModel.getXray_code());
                if (patientInfoModel != null) {
                    reportPublic.setPatientID(patientInfoModel.getMrn());
                    reportPublic.setPatientName(patientInfoModel.getName());
                    reportPublic.setGender(patientInfoModel.getSex().equals("M") ? "Nam" : "Nữ");
                    reportPublic.setDob(convertDateOfBirth(patientInfoModel.getBirth_Date()));
                    reportPublic.setAddress(patientInfoModel.getAddress());
                }
                reportPublic.setModality(codeModel == null ? null : codeModel.getXray_type_code());
                reportPublic.setAccession(acc);
                reportPublic.setDepartment(departmentModel == null ? null : departmentModel.getName_vie());
                reportPublic.setRefererrer(referrerModel.getName() + " " + referrerModel.getLastname());
                reportPublic.setPerformerCode(userModel.getLogin());
                reportPublic.setPerformerCchn(userModel.getDf_code());
                reportPublic.setPerformerName(userModel.getName() + userModel.getLastname());
                reportPublic.setTechnical(userModel1.getName());
                if (requestDetailModel.getWorkstation() != null) {
                    wsModel = webServiceXray.getWorkstation(requestDetailModel.getWorkstation());
                    reportPublic.setWorkstation(wsModel.getName());
                }
                if (requestDetailModel.getDoituong() != null) {
                    doituongModel = webServiceXray.getDoituong(requestDetailModel.getDoituong());
                    reportPublic.setCoverage_type(doituongModel.getName());
                }
                reportPublic.setNote(requestDetailModel.getGhichu());
                reportPublic.setReason(requestModel.getId() == null ? null : requestModel.getNote());
                reportPublic.setProcedure(codeModel == null ? null : codeModel.getDescription());
                if (requestDetailModel.getApproved_time() != null) {
                    reportPublic.setDatetime(requestDetailModel.getApproved_time().toString());
                } else {
                    reportPublic.setDatetime(report.getApprove_time().toString());
                }
                reportPublic.setDescribe(report.getDescribereport());
                reportPublic.setConclusion(report.getConclusion());
                reportPublic.setSuggestion(report.getNote());
                //reportPublic.setDataQR(encodeFileToBase64Binary("/var/www/html/QR/" + acc + ".png"));
            } else {
                UserModel userModel1 = webServiceXray.getUserByID((long) 3);
                requestModel = webServiceXray.getRequestByAcc(acc);
                requestDetailModel = webServiceXray.getRequestDetailsByAcc(acc);
                patientInfoModel = webServiceXray.getPatientByMrn(requestModel == null ? null : requestModel.getMrn());
                referrerModel = webServiceXray.getRefererrerByID(Long.valueOf(requestModel == null ? "1" : requestModel.getReferrer()));
                departmentModel = webServiceXray.getDepartmentBydepartID(requestModel == null ? null : requestModel.getDepartment_id());
                codeModel = webServiceXray.getXrayCodeByCode(requestDetailModel.getXray_code());
                reportPublic.setPatientID(patientInfoModel.getMrn());
                reportPublic.setPatientName(patientInfoModel.getName());
                reportPublic.setGender(patientInfoModel.getSex().equals("M") ? "Nam" : "Nữ");
                reportPublic.setDob(convertDateOfBirth(patientInfoModel.getBirth_Date()));
                reportPublic.setAddress(patientInfoModel.getAddress());
                reportPublic.setModality(codeModel.getXray_type_code());
                reportPublic.setAccession(acc);
                reportPublic.setDepartment(departmentModel.getName_vie());
                reportPublic.setRefererrer(referrerModel.getName() + " " + referrerModel.getLastname());
                reportPublic.setPerformerCode(null);
                reportPublic.setPerformerCchn(null);
                reportPublic.setPerformerName(null);
                reportPublic.setTechnical(userModel1.getName());
                if (requestDetailModel.getWorkstation() != null) {
                    wsModel = webServiceXray.getWorkstation(requestDetailModel.getWorkstation());
                    reportPublic.setWorkstation(wsModel.getName());
                }
                if (requestDetailModel.getDoituong() != null) {
                    doituongModel = webServiceXray.getDoituong(requestDetailModel.getDoituong());
                    reportPublic.setCoverage_type(doituongModel.getName());
                }
                reportPublic.setNote(requestDetailModel.getGhichu());
                reportPublic.setReason(requestModel.getNote());
                reportPublic.setProcedure(codeModel.getDescription());
                reportPublic.setDatetime(null);
                reportPublic.setDescribe(null);
                reportPublic.setConclusion(null);
                reportPublic.setSuggestion(null);
                reportPublic.setDataQR(null);
            }
        } catch (
                Exception e) {
            log.error("Error report Details!");
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reportPublic);
    }

    private static String sendReportBunble(String url, String jsonString, String acc, String user, String password) {
        String authStr = user + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        try {
            URL urlBundle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
            String token = "Basic " + new String(base64Creds);
            // Set timeout as per needs
            connection.setRequestProperty("Authorization", token);
            //System.out.println(token);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            // Set DoOutput to true if you want to use URLConnection for output.
            // Default is false
            connection.setDoOutput(true);

            connection.setUseCaches(true);
            connection.setRequestMethod("PUT");

            // Set Headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Write XML
            OutputStream outputStream = connection.getOutputStream();
            byte[] b = jsonString.getBytes("UTF-8");
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();

            // Read XML
            InputStream inputStream = connection.getInputStream();
            byte[] res = new byte[2048];
            int i = 0;
            StringBuilder response = new StringBuilder();
            while ((i = inputStream.read(res)) != -1) {
                response.append(new String(res, 0, i));
            }
            inputStream.close();
            log.info("Created Bundle Report acc {} Completed!", acc);
            try {
                WebClientConfig webClientConfig = new WebClientConfig();
                WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
                int statusCode = connection.getResponseCode();
                HisLog hisLog = new HisLog();
                hisLog.setId((long) 0);
                String bundle_ID = getBundleID(jsonString);
                hisLog.setBundle_id(bundle_ID);
                hisLog.setAccession(acc);
                if (statusCode == 200 || statusCode == 201) {
                    hisLog.setCode("0");
                } else {
                    hisLog.setCode("1");
                }
                hisLog.setMessage(response.toString());
                hisLog.setStatus(Integer.toString(statusCode));
                hisLog.setCreated_time(new Date());
                webServiceXray.createHisLog(hisLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        } catch (Exception e) {
            log.error("Error Create Bundle Report {}", acc);
            e.printStackTrace();
            return null;
        }
    }

    private static String deleteReportHIS(String url, String jsonString, String acc, String user, String password) {
        String authStr = user + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        try {
            URL urlBundle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
            String token = "Basic " + new String(base64Creds);
            // Set timeout as per needs
            connection.setRequestProperty("Authorization", token);
            //System.out.println(token);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            // Set DoOutput to true if you want to use URLConnection for output.
            // Default is false
            connection.setDoOutput(true);

            connection.setUseCaches(true);
            connection.setRequestMethod("DELETE");

            // Set Headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Write XML
            OutputStream outputStream = connection.getOutputStream();
            byte[] b = jsonString.getBytes("UTF-8");
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();

            // Read XML
            InputStream inputStream = connection.getInputStream();
            byte[] res = new byte[2048];
            int i = 0;
            StringBuilder response = new StringBuilder();
            while ((i = inputStream.read(res)) != -1) {
                response.append(new String(res, 0, i));
            }
            inputStream.close();
            log.info("Created Delete Report acc {} Completed!", acc);
            return response.toString();
        } catch (Exception e) {
            log.error("Error Delete Report {}", acc);
            e.printStackTrace();
            return null;
        }
    }

    private static String getBundleByIdentifer(String url, String user, String password) {
        String authStr = user + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        try {
            URL urlBundle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
            String token = "Basic " + new String(base64Creds);
            // Set timeout as per needs
            connection.setRequestProperty("Authorization", token);
            //System.out.println(token);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            // Set DoOutput to true if you want to use URLConnection for output.
            // Default is false
            connection.setDoOutput(true);

            connection.setUseCaches(true);
            connection.setRequestMethod("GET");

            // Set Headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            int responseCode = connection.getResponseCode();
            log.info("GET Response Code :: {} ", responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                log.info("Created Bundle Report url Completed!", url);
                return response.toString();
            } else {
                log.error("GET request did not work.");
                return null;
            }

        } catch (Exception e) {
            log.error("Error Create Bundle Report {}", url);
            e.printStackTrace();
            return null;
        }
    }

    private static String getBundleByID(String url, String user, String password) {
        String authStr = user + ":" + password;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        try {
            URL urlBundle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBundle.openConnection();
            String token = "Basic " + new String(base64Creds);
            // Set timeout as per needs
            connection.setRequestProperty("Authorization", token);
            //System.out.println(token);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            // Set DoOutput to true if you want to use URLConnection for output.
            // Default is false
            connection.setDoOutput(true);

            connection.setUseCaches(true);
            connection.setRequestMethod("GET");

            // Set Headers
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            int responseCode = connection.getResponseCode();
            log.info("GET Response Code :: {} ", responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                log.info("Created Bundle Report url Completed!", url);
                return response.toString();
            } else {
                log.error("GET request did not work.");
                return null;
            }

        } catch (Exception e) {
            log.error("Error Create Bundle Report {}", url);
            e.printStackTrace();
            return null;
        }
    }

    private static void copyInputStreamToFile(String directory, StringBuilder content, String acc) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(directory + acc + ".json");
        try {
            Writer targetFileWriter = new FileWriter(file);
            targetFileWriter.write(content.toString());
            targetFileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void copyReportRIS2HIS(String directory, StringBuilder content, String acc) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        String curr = getCurrentDate();
        String year = curr.substring(0, 4);
        String month = curr.substring(4, 6);
        String day = curr.substring(6, 8);
        File file = new File(directory + year + month + day + "_" + acc + ".json");
        String bundleResponse = "{}";
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        try {
            Bundle bundle = (Bundle) parser.parseResource(content.toString());
            bundleResponse = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Parse resouce error!");
        }
        try {
            Writer targetFileWriter = new FileWriter(file);
            targetFileWriter.write(bundleResponse);
            targetFileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void copyResponseHISToFile(String directory, StringBuilder content, String acc) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(directory + acc + ".txt");
        try {
            Writer targetFileWriter = new FileWriter(file);
            targetFileWriter.write(content.toString());
            targetFileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void copyJsonData(String directory, String content, String acc) {
        File dir = new File(directory + acc);
        if (!dir.exists()) dir.mkdirs();
        String path = directory + acc + "/" + "report_" + currentDate() + "_" + acc + ".json";
        try {
            Files.write(Paths.get(path), content.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static String currentDatetime() {
        String now = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        now = dateFormat.format(date);
        return now;
    }

    private static String currentDate() {
        String now = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        now = dateFormat.format(date);
        return now;
    }

    private static Date convertDatetime(String datetime) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    private static String convertDateOfBirth(Date datetime) {
        String dob = null;
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dob;
    }

    private static Date getDatetime() {
        Timestamp ts = null;
        try {
            ts = Timestamp.from(ZonedDateTime.now().toInstant());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    private static String encodeFileToBase64Binary(String fileName) throws IOException {
        byte[] encoded = null;
        try {
            File file = new File(fileName);
            encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Not found QR!");
            return null;
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static List<CE> extractCE(String conclusion, WebClientConfig webClientConfig) {
        List<CE> ces = new ArrayList<>();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        try {
            String clininalElements = conclusion.substring(conclusion.indexOf("CLINICAL ELEMENTS:") + 18, conclusion.length());
            clininalElements = clininalElements.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "");
            List<String> cons = new ArrayList<String>(Arrays.asList(clininalElements.split("\n")));
            for (int i = 0; i < cons.size(); i++) {
                CE ce = new CE();
                try {
                    String ce_name = cons.get(i).split(":")[0];
                    String ce_val = cons.get(i).split(":")[1];
                    ce_name = ce_name.trim();
                    ce_val = ce_val.trim();
                    ce_val = ce_val.replaceAll("\n", "");
                    ClinicalElement clinicalElement = webServiceXray.getCEByName(ce_name);
                    ce.setIndex(clinicalElement.getId().intValue());
                    ce.setValue(ce_val.toUpperCase());
                    ces.add(ce);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ces;
    }

    private static List<Integer> extractMP(String conclusion, WebClientConfig webClientConfig) {
        List<Integer> mps = new ArrayList<>();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        try {
            String majorProblem = conclusion.substring(conclusion.indexOf("CONCLUSION:") + 12, conclusion.indexOf("CLINICAL ELEMENTS:"));
            majorProblem = majorProblem.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "");
            List<String> cons = new ArrayList<String>(Arrays.asList(majorProblem.split("\n")));
            for (int i = 0; i < cons.size(); i++) {
                try {
                    List<String> keys = keys(cons.get(i));
                    for (String key : keys
                    ) {
                        if (key.equals("NULL")) {
                            continue;
                        }
                        MajorProblem mp = webServiceXray.getMPByName(key);
                        mps.add(mp.getId().intValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mps;
    }

    private static List<String> keys(String input) {
        List<String> mpList = new ArrayList<>();
        try {
            input = input.toUpperCase();
            if (input.contains("TUBERCULOSIS")) {
                mpList.add("Infiltrative lung tuberculosis");
            }
            if (input.contains("NODULE")) {
                if (input.contains("LUNG")) {
                    mpList.add("Nodule of lung");
                }
            }
            if (input.contains("PNEUMONIA")) {
                mpList.add("Pneumonia");
            }
            if (input.contains("PLEURAL EFFUSION")) {
                mpList.add("Pleural effusion");
            }
            if (input.contains("FIBROSIS")) {
                if (input.contains("LUNG")) {
                    mpList.add("Fibrosis of lung");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mpList.size() == 0) {
            mpList.add("NULL");
        }
        return mpList;
    }

    private static String getCurrentDate() {
        String current = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            current = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return current;
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

    private static String readFileReport(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();

            String content = stringBuilder.toString();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getBundleID(String resouce) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        try {
            Bundle bundle = (Bundle) parser.parseResource(resouce);
            Bundle.BundleEntryComponent subBundle = bundle.getEntry().get(bundle.getEntry().size() - 1);
            Bundle resourceCtx = (Bundle) subBundle.getResource();
            String contextID = resourceCtx.getIdBase();
            return contextID.substring(contextID.lastIndexOf("/") + 1, contextID.length());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String authenticate(String URL_HSM_AUTH, Authenticate authenticate) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Data attached to the request.
        HttpEntity<Authenticate> requestBody = new HttpEntity<>(authenticate, headers);

        // Send request with POST method.
        ResponseEntity<ResponseJWT> result
                = restTemplate.postForEntity(URL_HSM_AUTH, requestBody, ResponseJWT.class);

        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            ResponseJWT r = result.getBody();
            log.info("(Client Side) Authenticate Success.", r.getId_token());
            return r.getId_token();
        }
        return null;
    }
    private static Image getImage(String uri, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Image> response = restTemplate.exchange(uri, HttpMethod.GET, entity,
                Image.class);
        if (response.getStatusCodeValue() == 200) {
            return response.getBody();
        } else {
            log.info("(Client Side) Patient Info {} not exist.");
        }
        return null;
    }

    private static ResponseSignedPDF positionPdf(String URL_HSM_PDF, String accessToken, PDFSignPositionData pdfSignData) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Data attached to the request.
        HttpEntity<PDFSignPositionData> requestBody = new HttpEntity<>(pdfSignData, headers);
        // Send request with POST method.
        ResponseEntity<ResponseSignedPDF> result
                = restTemplate.postForEntity(URL_HSM_PDF, requestBody, ResponseSignedPDF.class);
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            ResponseSignedPDF r = result.getBody();
            log.info("(Client Side) positionPdf Success.");
            return r;
        }
        return null;
    }

    private static ResponseSignedPDF invisiblePdf(String URL_HSM_PDF, String accessToken, PDFSignData pdfSignData) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Data attached to the request.
        HttpEntity<PDFSignData> requestBody = new HttpEntity<>(pdfSignData, headers);
        // Send request with POST method.
        ResponseEntity<ResponseSignedPDF> result
                = restTemplate.postForEntity(URL_HSM_PDF, requestBody, ResponseSignedPDF.class);
        // Code = 200.
        if (result.getStatusCode() == HttpStatus.OK) {
            ResponseSignedPDF r = result.getBody();
            log.info("(Client Side) invisiblePdf Success.");
            return r;
        }
        return null;
    }

    private static String encodeFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }

    private static void base64ToFile(String file, String data) {
        File filePath = new File(file);

        try ( FileOutputStream fos = new FileOutputStream(filePath); ) {
            byte[] decoder = Base64.getDecoder().decode(data);
            fos.write(decoder);
            System.out.println("PDF File Saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTimeDigit() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HHmmss");
            Date date = new Date();
            return dateFormat.format(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "bk";
    }
    public void main(String[] args) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        String acc = "231102833167";
        UserModel userModel;
        RequestModel requestModel = null;
        RequestDetailModel requestDetailModel;
        PatientInfoModel patientInfoModel;
        ReferrerModel referrerModel = null;
        DepartmentModel departmentModel;
        CodeModel codeModel;
        WSModel wsModel;
        DoituongModel doituongModel;
        try {
            ReportModel report = reportService.getReportByAcc(acc);
            if (!report.getApprove_by().equals("null")) {
                userModel = webServiceXray.getUserByID(Long.valueOf(report.getApprove_by()));
            } else {
                userModel = webServiceXray.getUserByID(Long.valueOf("2"));
            }
            UserModel userModel1 = webServiceXray.getUserByID((long) 3);
            requestModel = webServiceXray.getRequestByAcc(acc);
            requestDetailModel = webServiceXray.getRequestDetailsByAcc(acc);
            patientInfoModel = requestModel.getId() == null ? null : webServiceXray.getPatientByMrn(requestModel.getMrn());
            try {
                referrerModel = webServiceXray.getRefererrerByRefID(requestModel.getReferrer());
            } catch (Exception e) {
                e.printStackTrace();
                referrerModel = webServiceXray.getRefererrerByID(Long.valueOf(requestModel.getId() == null ? "1" : requestModel.getReferrer()));
            }
            departmentModel = webServiceXray.getDepartmentBydepartID(requestModel.getId() == null ? "001" : requestModel.getDepartment_id());
            codeModel = requestDetailModel.getId() == null ? null : webServiceXray.getXrayCodeByCode(requestDetailModel.getXray_code());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    public static <T> boolean IsNullOrEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }
}
