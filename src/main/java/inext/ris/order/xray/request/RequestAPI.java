package inext.ris.order.xray.request;

import java.math.BigInteger;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import inext.ris.order.xray.department.DepartmentModel;
import inext.ris.order.xray.his.model.encounter.EncounterData;
import inext.ris.order.xray.his.model.encounter.EncounterDiagnosis;
import inext.ris.order.xray.his.model.encounter.EncounterPractitioner;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceHapi;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.referrer.ReferrerModel;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/request")
@Slf4j
@RequiredArgsConstructor
public class RequestAPI {
    @Autowired
    RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestModel>> findAll() {
        return ResponseEntity.ok(requestService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody RequestModel request) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestService.existsByMRN(request.getRequest_no()).compareTo(bi);
        try {
            String note = request.getNote();
            Boolean checkNote = checkIfNullOrEmpty(note);
            note = checkNote ? note:" ";
            request.setNote(note);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Find character non UNICODE!!");
        }
        if (res > 0) {
            log.error("REQUEST_NO " + request.getRequest_no() + " does existed");
            return ResponseEntity.badRequest().build();
        } else {
            String acc = request.getRequest_no();
            WebClientConfig webClientConfig = new WebClientConfig();
            WebServiceHapi webServiceHapi = new WebServiceHapi(webClientConfig.hapiApiClient());
            WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
            try {
                String datereq = convertTimestampToString(request.getRequest_timestamp());
                String medicalRecordNumber = acc;
                String admitNumber = acc + "_" + datereq;
                String managementNumber = acc + "_" + datereq;
                String subject = request.getMrn();
                String classOrigin = "AMB";
                String admitDatetime = requestDatetime(request.getRequest_timestamp());
                DepartmentModel departmentModel = webServiceXray.getDepartmentBydepartID(request.getDepartment_id());
                String codeDepartment = Base64.getEncoder().encodeToString(departmentModel.getName_vie().getBytes());
                String locationWardCode = codeDepartment;
                String datetime = admitDatetime;
                List<EncounterDiagnosis> encounterDiagnosisList = new ArrayList<>();
                EncounterDiagnosis encounterDiagnosis = new EncounterDiagnosis();
                encounterDiagnosis.setType("AD");
                encounterDiagnosis.setIcd(" ");
                encounterDiagnosis.setDiagnosis(request.getNote());
                encounterDiagnosisList.add(encounterDiagnosis);
                List<EncounterPractitioner> encounterPractitionerList = new ArrayList<>();
                EncounterPractitioner encounterPractitionerADM = new EncounterPractitioner();
                encounterPractitionerADM.setCode("ADM");
                encounterPractitionerADM.setText("admitter");
                ReferrerModel referrerModel = webServiceXray.getRefererrerByRefID(request.getReferrer());
                encounterPractitionerADM.setPractitioner(referrerModel.getPractitioner_id());
                encounterPractitionerList.add(encounterPractitionerADM);
                String organizationCode = "00000000";

                EncounterData encounterData = new EncounterData();
                encounterData.setMedicalRecordNumber(medicalRecordNumber);
                encounterData.setAdmitNumber(admitNumber);
                encounterData.setManagementNumber(managementNumber);
                encounterData.setSubject(subject);
                encounterData.setClassOrigin(classOrigin);
                encounterData.setAdmitDatetime(admitDatetime);
                encounterData.setLocationWardCode(locationWardCode);
                encounterData.setDatetime(datetime);
                encounterData.setEncounterDiagnosisList(encounterDiagnosisList);
                encounterData.setLocationTransferCode("");
                encounterData.setEncounterPractitionerList(encounterPractitionerList);
                encounterData.setOrganizationCode(organizationCode);
                String encounterUUID = webServiceHapi.createEncounter(encounterData);
                log.info("Create Encounter {}!", encounterUUID);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error create Encounter {}", request.getRequest_no());
            }
        }
        return ResponseEntity.ok(requestService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestModel> findById(@PathVariable Long id) {
        Optional<RequestModel> request = requestService.findById(id);
        if (!request.isPresent()) {
            log.error("Request Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(request.get());
    }

    @GetMapping("/accession/{acc}")
    public ResponseEntity<RequestModel> findByAccession(@PathVariable String acc) {
        RequestModel request = requestService.getRequestByAcc(acc);
        if (request.getRequest_no().isEmpty()) {
            log.error("Request ACCESSION " + acc + " does not existed");
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request);
    }


    @GetMapping("/check/accession/{mrn}/{acc}")
    public Boolean checkByAccession(@PathVariable String mrn, @PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestService.existsByAccNoAndMrn(mrn, acc).compareTo(bi);
        if (res <= 0) {
            log.error("Check Request with acc " + acc + " is not existed");
            return false;
        }
        return true;
    }
    @GetMapping("/study/{from}/{to}")
    public ResponseEntity<List<String>> listStudy(@PathVariable String from, @PathVariable String to) {
        Date fromDate = convertLocalDate(from);
        Date toDate = convertLocalDate(to);
        List<String> studyModels = requestService.listStudy(fromDate, toDate);
        return ResponseEntity.ok(studyModels);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RequestModel> update(@PathVariable Long id, @Valid @RequestBody RequestModel request) {
        if (!requestService.findById(id).isPresent()) {
            log.error("Request Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }
        RequestModel requestUpdate = requestService.getOne(id);
        requestUpdate.setXn(request.getXn());
        requestUpdate.setMrn(request.getMrn());
        requestUpdate.setReferrer(request.getReferrer());
        requestUpdate.setRequest_date(request.getRequest_date());
        requestUpdate.setDepartment_id(request.getDepartment_id());
        requestUpdate.setPortable(request.getPortable());
        requestUpdate.setUser(request.getUser());
        requestUpdate.setCancel_status(request.getCancel_status());
        requestUpdate.setUser_id_cancle(request.getUser_id_cancle());
        requestUpdate.setCancel_date(request.getCancel_date());
        requestUpdate.setCancel_time(request.getCancel_time());
        requestUpdate.setStatus(request.getStatus());
        requestUpdate.setIcon(request.getIcon());
        requestUpdate.setNote(request.getNote());
        requestUpdate.setCenter_code(request.getCenter_code());
        return ResponseEntity.ok(requestService.save(requestUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!requestService.findById(id).isPresent()) {
            log.error("Request Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }

        requestService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    private static Date convertLocalDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    private static String convertTimestampToString(Date timestamp) {
        //System.out.println("before:"+timestamp);
        String datetime = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //newdf.setTimeZone(TimeZone.getTimeZone("UTC"));;
        SimpleDateFormat newdfRp = new SimpleDateFormat("yyyyMMdd");
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

    private static String requestDatetime(Date timestamp) {
        String datetime = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String temp = newdf.format(timestamp);
            Date tempDate = newdf.parse(temp);
            datetime = newdf.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetime;
    }

    private static boolean checkIfNullOrEmpty(String reason) {
        try {
            if (reason == null) {
               return false;
            }
            else if (StringUtils.isEmpty(reason)) {
                return false;
            }
            else if (StringUtils.isBlank(reason)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    /*public static void main(String[] args) {
        //String text = "rong huyết";
        //System.out.println(unaccent(text));
        //System.out.println(checkIfNullOrEmpty("  a"));
    }*/

}
