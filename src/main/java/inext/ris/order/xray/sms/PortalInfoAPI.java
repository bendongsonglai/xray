package inext.ris.order.xray.sms;

import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServicePortal;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.patient_info.PatientInfoModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/inext/portal")
@Slf4j
@RequiredArgsConstructor
public class PortalInfoAPI {
    @Autowired
    IPService ipService;

    @RequestMapping(value = "/mrn/{mrn}", method = RequestMethod.GET)
    public ResponseEntity<?> findByCode(@PathVariable String mrn, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP get portal {}", ip);
        if (Mrn_Validation(mrn)) {
            return new ResponseEntity<String>("bad mrn", HttpStatus.BAD_REQUEST);
        }
        BigInteger bi = new BigInteger("0");
        int res;
        res = ipService.existsByIP(ip).compareTo(bi);
        if (res <= 0) {
            log.error("IP not Enable " + mrn + " is not existed");
            return new ResponseEntity<String>("ip invalid", HttpStatus.CONFLICT);
        }
        log.info("MRN get account portal {}", mrn);
        IP ipModel = ipService.getPortalByIp(ip);
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        WebServicePortal webServicePortal = new WebServicePortal();
        try {
            String secretkey = webServiceXray.getAes().getSecret();
            String link = ipModel.getLink();
            //String token = inext.ris.order.xray.qr.AES.encrypt(mrn, secretkey);
            Account account = webServicePortal.getPortalInfo(mrn);
            String token = inext.ris.order.xray.qr.AES.encrypt(mrn+"^"+account.getSecret(), secretkey);
            PatientInfoModel patientInfoModel = webServiceXray.getPatientByMrn(mrn);
            PatientPortal patientPortal = new PatientPortal();
            patientPortal.setMrn(mrn);
            patientPortal.setName(patientInfoModel.getName() + patientInfoModel.getLastName());
            patientPortal.setEmail(account.getEmail());
            patientPortal.setTelephone(account.getTelephone());
            patientPortal.setSecret(account.getSecret());
            patientPortal.setOtp(account.getOtp());
            patientPortal.setLinkportal(link + token);
            return new ResponseEntity<PatientPortal>(patientPortal, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("portal not found", HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "/reset/{mrn}", method = RequestMethod.POST)
    public ResponseEntity<?> ResetPass(@PathVariable String mrn, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP get portal {}", ip);
        if (Mrn_Validation(mrn)) {
            return new ResponseEntity<String>("bad mrn", HttpStatus.BAD_REQUEST);
        }
        BigInteger bi = new BigInteger("0");
        int res;
        res = ipService.existsByIP(ip).compareTo(bi);
        if (res <= 0) {
            log.error("IP not Enable " + mrn + " is not existed");
            return new ResponseEntity<String>("ip invalid", HttpStatus.CONFLICT);
        }
        log.info("MRN reset password {}", mrn);
        IP ipModel = ipService.getPortalByIp(ip);
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        WebServicePortal webServicePortal = new WebServicePortal();
        try {
            webServicePortal.ResetPassword(mrn);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {

            String secretkey = webServiceXray.getAes().getSecret();
            String link = ipModel.getLink();
            //String token = inext.ris.order.xray.qr.AES.encrypt(mrn, secretkey);
            Account account = webServicePortal.getPortalInfo(mrn);
            String token = inext.ris.order.xray.qr.AES.encrypt(mrn+"^"+account.getSecret(), secretkey);
            PatientInfoModel patientInfoModel = webServiceXray.getPatientByMrn(mrn);
            PatientPortal patientPortal = new PatientPortal();
            patientPortal.setMrn(mrn);
            patientPortal.setName(patientInfoModel.getName() + patientInfoModel.getLastName());
            patientPortal.setEmail(account.getEmail());
            patientPortal.setTelephone(account.getTelephone());
            patientPortal.setSecret(account.getSecret());
            patientPortal.setOtp(account.getOtp());
            patientPortal.setLinkportal(link + token);
            return new ResponseEntity<PatientPortal>(patientPortal, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("portal not found", HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public ResponseEntity<?> CreateMessage(@RequestBody Message message, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP get portal {}", ip);
        if (message == null) {
            return new ResponseEntity<String>("bad body", HttpStatus.BAD_REQUEST);
        }
        String mrn = message.getMrn();
        if (Mrn_Validation(mrn)) {
            return new ResponseEntity<String>("bad mrn", HttpStatus.BAD_REQUEST);
        }
        if (message.getSender() == "" || message.getSender() == null) {
            return new ResponseEntity<String>("Check if title is empty, not just null", HttpStatus.BAD_REQUEST);
        }
        BigInteger bi = new BigInteger("0");
        int res;
        res = ipService.existsByIP(ip).compareTo(bi);
        if (res <= 0) {
            log.error("IP not Enable " + mrn + " is not existed");
            return new ResponseEntity<String>("ip invalid", HttpStatus.CONFLICT);
        }
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServicePortal webServicePortal = new WebServicePortal();
        try {
            String sender = message.getSender();
            String publisher = sender.substring(0, sender.indexOf("^"));
            String physicans = sender.substring(sender.indexOf("^") + 1, sender.length());
            MessagePublic messagePublic = new MessagePublic();
            messagePublic.setMessage_id(message.getMessage_id());
            messagePublic.setMrn(message.getMrn());
            messagePublic.setPublisher(publisher);
            messagePublic.setPhysicans(physicans);
            messagePublic.setTitle(message.getTitle());
            messagePublic.setPurpose(message.getPurpose());
            messagePublic.setDescription(message.getDescription());
            messagePublic.setCreated(message.getCreated());
            ResponseMessage responseMessage = webServicePortal.createMessage(messagePublic);

            return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("portal not found", HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "/message/{message_id}", method = RequestMethod.POST)
    public ResponseEntity<?> CreateMessageReply(@PathVariable String message_id, @RequestBody Reply reply, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP get portal {}", ip);
        BigInteger bi = new BigInteger("0");
        int res;
        res = ipService.existsByIP(ip).compareTo(bi);
        if (res <= 0) {
            log.error("IP not Enable is not existed");
            return new ResponseEntity<String>("ip invalid", HttpStatus.CONFLICT);
        }
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServicePortal webServicePortal = new WebServicePortal();
        try {
            PortalMessage portalMessage = webServicePortal.getPortalMessage(message_id);
            PortalReply portalReply = new PortalReply();
            portalReply.setId((long) 0);
            portalReply.setMessage_uid(portalMessage.getMessage_uid());
            portalReply.setBy("PHYSICANS");
            portalReply.setDatetime(reply.getDatetime());
            portalReply.setDescription(reply.getDescription());
            webServicePortal.replyMessage(portalReply);
            return new ResponseEntity<String>("Reply Message Success!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("portal not found", HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "/remove/{mrn}/{acc}", method = RequestMethod.POST)
    public ResponseEntity<?> ResetPass(@PathVariable String mrn, @PathVariable String acc, HttpServletRequest request) {
        String ip = getClientIp(request);
        log.info("IP get portal {}", ip);
        if (Mrn_Validation(mrn)) {
            return new ResponseEntity<String>("bad mrn", HttpStatus.BAD_REQUEST);
        }
        BigInteger bi = new BigInteger("0");
        int res;
        res = ipService.existsByIP(ip).compareTo(bi);
        if (res <= 0) {
            log.error("IP not Enable " + mrn + " is not existed");
            return new ResponseEntity<String>("ip invalid", HttpStatus.CONFLICT);
        }
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        boolean checkAcc = webServiceXray.checkRequestByAccAndMrn(mrn, acc);
        if(!checkAcc) {
            return new ResponseEntity<String>("Accession not found", HttpStatus.BAD_REQUEST);
        }

        WebServicePortal webServicePortal = new WebServicePortal();
        
        if (webServicePortal.checkAccession(acc)) {
            return new ResponseEntity<String>("Accession removed", HttpStatus.OK);
        }

        RecordsRemove remove = new RecordsRemove();
        try {
            remove.setId((long) 0);
            remove.setMrn(mrn);
            remove.setAccession(acc);
            remove.setRemoved(0);
            webServicePortal.removeRecord(remove);
            return new ResponseEntity<String>("Remove success", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Accession not found", HttpStatus.BAD_REQUEST);
        }
    }

    private static boolean Mrn_Validation(String mrn) {

        if (mrn.length() >= 2) {
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasSpecial = special.matcher(mrn);

            return hasSpecial.find() && mrn.contains(" ");
        } else
            return false;

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
}
