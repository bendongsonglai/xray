package inext.ris.order.xray.request_detail;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import inext.ris.order.xray.code.CodeModel;
import inext.ris.order.xray.his.model.ConceptCode;
import inext.ris.order.xray.his.model.ConceptDiagnosis;
import inext.ris.order.xray.his.model.ImagingData;
import inext.ris.order.xray.his.model.serviceRequest.IdentifierServiceRequest;
import inext.ris.order.xray.his.model.serviceRequest.ServiceRequestData;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceHapi;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.request.RequestModel;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/requestdetail")
@Slf4j
@RequiredArgsConstructor
public class RequestDetailAPI {
	@Autowired
	RequestDetailService requestDetailService;
	
	@GetMapping
    public ResponseEntity<List<RequestDetailModel>> findAll() {
        return ResponseEntity.ok(requestDetailService.findAll());
    }
	
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody RequestDetailModel request) {
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = requestDetailService.existsByAccession(request.getAccession()).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION " + request.getRequest_no() + " does existed");
            return ResponseEntity.badRequest().build();
        } else {
            String acc = request.getRequest_no();
            WebClientConfig webClientConfig = new WebClientConfig();
            WebServiceHapi webServiceHapi = new WebServiceHapi(webClientConfig.hapiApiClient());
            WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
            ServiceRequestData serviceRequestData = new ServiceRequestData();
            try {
                RequestModel requestModel = webServiceXray.getRequestByAcc(acc);
                List<IdentifierServiceRequest> identifierServiceRequests = new ArrayList<>();
                IdentifierServiceRequest identifierServiceRequest= new IdentifierServiceRequest();
                identifierServiceRequest.setUse("OFFICIAL");
                ConceptCode type = new ConceptCode();
                type.setSystem("idchidinh");
                type.setCode("idchidinh");
                type.setValue("Primary Identifier Order");
                type.setText("Primary Identifier Order");
                identifierServiceRequest.setType(type);
                identifierServiceRequest.setSystem("idchidinh");
                identifierServiceRequest.setValue(acc);
                identifierServiceRequests.add(identifierServiceRequest);
                serviceRequestData.setIdentifierServiceRequests(identifierServiceRequests);
                List<ConceptCode> category = new ArrayList<>();
                ConceptCode category1 = new ConceptCode();
                category1.setSystem("http://snomed.info/sct");
                category1.setCode("363680008");
                category1.setValue("Radiographic imaging procedure");
                category1.setText("Radiographic imaging procedure");
                category.add(category1);
                CodeModel codeModel = webServiceXray.getXrayCodeByCode(request.getXray_code());
                ConceptCode category2 = new ConceptCode();
                category2.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-procedureCode");
                category2.setCode(codeModel.getXray_type_code());
                category2.setValue(codeModel.getBody_part());
                category2.setText(codeModel.getBody_part());
                category.add(category2);
                serviceRequestData.setCategory(category);
                IdentifierServiceRequest requisition = new IdentifierServiceRequest();
                requisition.setUse("OFFICIAL");
                ConceptCode typeModality = new ConceptCode();
                typeModality.setSystem("idVchidinh");
                typeModality.setCode("idVchidinh");
                typeModality.setValue("Secondary Identifier Order");
                typeModality.setText("Secondary Identifier Order");
                requisition.setType(typeModality);
                requisition.setSystem("");
                requisition.setValue(acc);
                serviceRequestData.setRequisition(requisition);
                ConceptCode code = new ConceptCode();
                code.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-procedureCode");
                code.setCode(codeModel.getXray_code());
                code.setValue(codeModel.getDescription());
                code.setText(codeModel.getDescription());
                serviceRequestData.setCode(code);
                serviceRequestData.setSubject(requestModel.getMrn());
                serviceRequestData.setEncounter(acc);
                serviceRequestData.setAuthoredOn(requestDatetime(requestModel.getRequest_timestamp()));
                serviceRequestData.setOccurrenceDateTime(requestDatetime(requestModel.getRequest_timestamp()));
                serviceRequestData.setPerformer(requestModel.getReferrer());
                serviceRequestData.setRequester(requestModel.getReferrer());
                ConceptDiagnosis reason = new ConceptDiagnosis();
                reason.setCode("NULL");
                reason.setText(requestModel.getNote());
                serviceRequestData.setReason(reason);
                serviceRequestData.setQuantity("1");
                String serviceUUID = webServiceHapi.createServiceRequest(serviceRequestData);
                ImagingData imagingData = new ImagingData();
                imagingData.setAccessionNumber(acc);
                imagingData.setModality(codeModel.getXray_type_code());
                imagingData.setDescription(codeModel.getDescription());
                imagingData.setActor(requestModel.getReferrer());
                imagingData.setSubject(requestModel.getMrn());
                webServiceHapi.createImagingStudy(imagingData);
            log.info("Create ServiceRequest & ImagingStudy acc {}!", acc);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error create ServiceRequest & ImagingStudy {}", request.getRequest_no());
            }
        }
        return ResponseEntity.ok(requestDetailService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDetailModel> findById(@PathVariable Long id) {
        Optional<RequestDetailModel> request = requestDetailService.findById(id);
        if (!request.isPresent()) {
            log.error("RequestDetail Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(request.get());
    }
    
    @GetMapping("/accession/{acc}")
    public ResponseEntity<RequestDetailModel> findByAccession(@PathVariable String acc) {
        RequestDetailModel request = requestDetailService.getRequestDetailByAccesion(acc);
        if (request.getAccession().isEmpty()) {
            log.error("RequestDetail ACCESSION " + acc + " does not existed");
          return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("/check/{acc}")
    public Boolean checkRequestDetail(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + acc + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/checkstudy/{acc}")
    public Boolean checkRequestDetailStudy(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccNoAndImage(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + acc + " is not existed");
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/room/{accession}/{status}/{page}/{tech1}", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateRequestDetailByAccession(@PathVariable String accession, @PathVariable String status, @PathVariable String page, @PathVariable String tech1) {
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = requestDetailService.existsByAccession(accession).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + accession + " does not existed");
            ResponseEntity.notFound().build();
        }
        if (status.isEmpty()) {
            log.error("RequestDetail Status Empty.");
            ResponseEntity.badRequest().build();
        }
        requestDetailService.updateRequestDetailByAccession(status, page, tech1, tech1, getDateTimeNow(), getDateTimeNow(), accession);
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(accession));
    }

    @RequestMapping(value = "/final/{acc}", method = RequestMethod.POST)
    public ResponseEntity<RequestDetailModel> updateToReportFinal(@Valid @RequestBody RequestDetailModel request) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(request.getAccession()).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + request.getAccession() + " does not existed");
            return ResponseEntity.notFound().build();
        }
        if (request.getStatus()== null) {
            log.error("RequestDetail Status null.");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(requestDetailService.save(request));
    }
    @RequestMapping(value = "/final", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateFinalByAccession(@Valid @RequestBody RequestDetailModel request) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(request.getAccession()).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + request.getAccession() + " does not existed");
            return  ResponseEntity.notFound().build();
        }
        if (request.getStatus()== null) {
            log.error("RequestDetail Status null.");
            return ResponseEntity.badRequest().build();
        }
        requestDetailService.updateToReportByAccession(request.getStatus(), request.getPage(),  request.getAssign(), getDateTimeNow(),request.getTemp_report(), request.getAccession());
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(request.getAccession()));
    }

    @RequestMapping(value = "/toreport/{acc}", method = RequestMethod.POST)
    public ResponseEntity<RequestDetailModel> updateToReport(@Valid @RequestBody RequestDetailModel request) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(request.getAccession()).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + request.getAccession() + " does not existed");
            return  ResponseEntity.notFound().build();
        }
        if (request.getStatus()== null) {
            log.error("RequestDetail Status null.");
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(requestDetailService.save(request));
    }
    @RequestMapping(value = "/toreport", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateToReportByAccession(@Valid @RequestBody RequestDetailModel request) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(request.getAccession()).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + request.getAccession() + " does not existed");
            return  ResponseEntity.notFound().build();
        }
        if (request.getStatus()== null) {
            log.error("RequestDetail Status null.");
            return  ResponseEntity.badRequest().build();
        }
        requestDetailService.updateToReportByAccession(request.getStatus(), request.getPage(),  request.getAssign(), getDateTimeNow(),request.getTemp_report(), request.getAccession());
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(request.getAccession()));
    }

    @RequestMapping(value = "/control/{flag1}/{acc}", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateCtr(@PathVariable String  flag1, @PathVariable String  acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + acc + " does not existed");
            return ResponseEntity.notFound().build();
        }
        requestDetailService.updateOrderControl(flag1, acc);
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(acc));
    }

    @RequestMapping(value = "/controlnormal/{flag3}/{acc}", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateCtrNormal(@PathVariable String  flag3, @PathVariable String  acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + acc + " does not existed");
            return ResponseEntity.notFound().build();
        }
        requestDetailService.updateOrderControlNormal(flag3, acc);
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(acc));
    }

    @RequestMapping(value = "/approved/{acc}", method = RequestMethod.POST)
    public ResponseEntity<RequestDetailModel> approved(@PathVariable String  acc, @Valid @RequestBody RequestDetailModel request) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + request.getAccession() + " does not existed");
            return  ResponseEntity.notFound().build();
        }
        RequestDetailModel requestDetailUpdate =  requestDetailService.getRequestDetailByAccesion(acc);
        requestDetailUpdate.setReport_status(request.getReport_status());
        requestDetailUpdate.setApproved_time(request.getApproved_time());
        requestDetailUpdate.setAssign(request.getAssign());
        requestDetailUpdate.setAssign_by(request.getAssign_by());
        requestDetailUpdate.setStatus(request.getStatus());
        requestDetailUpdate.setPage(request.getPage());
        requestDetailUpdate.setLockby(request.getLockby());
        requestDetailUpdate.setTemp_report(request.getTemp_report());
        requestDetailUpdate.setFlag3(request.getFlag3());
        requestDetailUpdate.setReport_book(request.getReport_book());
        return ResponseEntity.ok(requestDetailService.save(requestDetailUpdate));
    }


    @RequestMapping(value = "/workstation/{wsid}/{acc}", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateWorkstation(@PathVariable String wsid, @PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + acc + " does not existed");
            return  ResponseEntity.notFound().build();
        }
        requestDetailService.updateWorkstation(wsid, acc);
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(acc));
    }

    @RequestMapping(value = "/study/{acc}/{total}/{status}/{studydate}", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> updateWorkstation(@PathVariable String acc, @PathVariable String total,
                                                                  @PathVariable String status, @PathVariable String studydate) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = requestDetailService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION " + acc + " does not existed");
            return  ResponseEntity.badRequest().build();
        }
        Date studyDate = null;
        try {
            studyDate = convertTimestamp(studydate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Datetime:"+studyDate);
        requestDetailService.updateStudy(total,status, studyDate, acc);
        return ResponseEntity.ok(requestDetailService.getRequestDetailByAccesion(acc));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RequestDetailModel> update(@PathVariable Long id, @Valid @RequestBody RequestDetailModel request) {
        if (!requestDetailService.findById(id).isPresent()) {
            log.error("RequestDetail Id " + id + " does not existed");
            return  ResponseEntity.badRequest().build();
        }
        RequestDetailModel requestDetailUpdate =  requestDetailService.getOne(id);
        requestDetailUpdate.setRequest_no(request.getRequest_no());
        requestDetailUpdate.setXray_code(request.getXray_code());
        requestDetailUpdate.setWorkstation(request.getWorkstation());
        requestDetailUpdate.setDoituong(request.getDoituong());
        requestDetailUpdate.setGhichu(request.getGhichu());
        requestDetailUpdate.setCharged(request.getCharged());
        requestDetailUpdate.setRequest_time(request.getRequest_time());
        requestDetailUpdate.setRequest_date(request.getRequest_date());
        requestDetailUpdate.setSchedule_date(request.getSchedule_date());
        requestDetailUpdate.setSchedule_time(request.getSchedule_time());
        requestDetailUpdate.setReport_time(request.getReport_time());
        requestDetailUpdate.setReport_date(request.getReport_date());
        requestDetailUpdate.setReport_status(request.getReport_status());
        requestDetailUpdate.setCancel_status(request.getCancel_status());
        requestDetailUpdate.setUser_id_cancel(request.getUser_id_cancel());
        requestDetailUpdate.setArrival_time(request.getArrival_time());
        requestDetailUpdate.setReady_time(request.getReady_time());
        requestDetailUpdate.setStart_time(request.getStart_time());
        requestDetailUpdate.setComplete_time(request.getComplete_time());
        requestDetailUpdate.setAssign_time(request.getAssign_time());
        requestDetailUpdate.setApproved_time(request.getApproved_time());
        requestDetailUpdate.setAssign(request.getAssign());
        requestDetailUpdate.setAssign_by(request.getAssign_by());
        requestDetailUpdate.setStatus(request.getStatus());
        requestDetailUpdate.setPage(request.getPage());
        requestDetailUpdate.setLockby(request.getLockby());
        requestDetailUpdate.setUrgent(request.getUrgent());
        requestDetailUpdate.setLastreport_id(request.getLastreport_id());
        requestDetailUpdate.setTemp_report(request.getTemp_report());
        requestDetailUpdate.setAuto_save_time(request.getAuto_save_time());
        requestDetailUpdate.setTech1(request.getTech1());
        requestDetailUpdate.setTech2(request.getTech2());
        requestDetailUpdate.setTech3(request.getTech3());
        requestDetailUpdate.setFlag1(request.getFlag1());
        requestDetailUpdate.setFlag2(request.getFlag2());
        requestDetailUpdate.setFlag3(request.getFlag3());
        requestDetailUpdate.setReport_book(request.getReport_book());
        return ResponseEntity.ok(requestDetailService.save(requestDetailUpdate));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!requestDetailService.findById(id).isPresent()) {
            log.error("RequestDetail Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }

        requestDetailService.deleteById(id);

        return ResponseEntity.ok().build();
    }
    @SuppressWarnings("unused")
	private static String getDateTimeNow() {
    String current = null;
	Date date = new Date();
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 	   current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(date.getTime()));
    return current;
    }
	@SuppressWarnings("unused")
	private static String getDateNow() {
	String current = null;
    long millis=System.currentTimeMillis();  
    java.sql.Date date=new java.sql.Date(millis);  
	current = new SimpleDateFormat("yyyy-MM-dd").format(date);
	return current;
	}
	@SuppressWarnings("unused")
	private static String getTimeNow() {
	String current = null;
    long millis=System.currentTimeMillis();  
    java.sql.Time time =new java.sql.Time(millis);  
	current = new SimpleDateFormat("HH:mm:ss").format(time);
	return current;
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

    private static Date convertTimestamp(String timestamp) {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=formatter.parse(timestamp);
            return date;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
