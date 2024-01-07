package inext.ris.order.xray.referrer;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import inext.ris.order.xray.his.model.PractitionerData;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceHapi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/referrer")
@Slf4j
@RequiredArgsConstructor
public class ReferrerAPI {
    @Autowired
    ReferrerService referrerService;

    @GetMapping
    public ResponseEntity<List<ReferrerModel>> findAll() {
        return ResponseEntity.ok(referrerService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ReferrerModel referrer) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = referrerService.existsByPrac(referrer.getPractitioner_id()).compareTo(bi);
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceHapi webServiceHapi = new WebServiceHapi(webClientConfig.hapiApiClient());
        if (res > 0) {
            log.error("PRACTITIONER_ID " + referrer.getPractitioner_id() + " existed");
            try {
                String mabs = referrer.getPractitioner_id();
                Boolean checkPractitioner = webServiceHapi.checkPractitioner(mabs);
                if (!checkPractitioner) {
                    PractitionerData practitionerData = new PractitionerData();
                    String certification = referrer.getPractitioner_id();
                    String name = referrer.getName() + " " + referrer.getLastname();
                    String phone = "";
                    String address = "";
                    String gender = referrer.getSex();
                    String organizationCode = "00000000";
                    practitionerData.setCertification(certification);
                    practitionerData.setName(name);
                    practitionerData.setMabs(mabs);
                    practitionerData.setPhone(phone);
                    practitionerData.setAddress(address);
                    practitionerData.setGender(gender);
                    practitionerData.setOrganization(organizationCode);
                    String practitionerUUID = webServiceHapi.createPractitioner(practitionerData);
                    log.info("Update Practitioner {}!", practitionerUUID);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error update Practitioner {}", referrer.getPractitioner_id());
            }
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            try {
                String mabs = referrer.getPractitioner_id();
                Boolean checkPractitioner = webServiceHapi.checkPractitioner(mabs);
                if (!checkPractitioner) {
                    PractitionerData practitionerData = new PractitionerData();
                    String certification = referrer.getPractitioner_id();
                    String name = referrer.getName() + " " + referrer.getLastname();
                    String phone = "";
                    String address = "";
                    String gender = referrer.getSex();
                    String organizationCode = "00000000";
                    practitionerData.setCertification(certification);
                    practitionerData.setName(name);
                    practitionerData.setMabs(mabs);
                    practitionerData.setPhone(phone);
                    practitionerData.setAddress(address);
                    practitionerData.setGender(gender);
                    practitionerData.setOrganization(organizationCode);
                    String locationUUID = webServiceHapi.createPractitioner(practitionerData);
                    log.info("Create Practitioner!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error create Practitioner {}", referrer.getPractitioner_id());
            }
        }
        return ResponseEntity.ok(referrerService.save(referrer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReferrerModel> findById(@PathVariable Long id) {
        Optional<ReferrerModel> referrer = referrerService.findById(id);
        if (!referrer.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(referrer.get());
    }

    @GetMapping("/practitioner/{prac}")
    public ResponseEntity<ReferrerModel> findByPrac(@PathVariable String prac) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(prac));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = referrerService.existsByPrac(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("PRACTITIONER_ID " + decodedString + " is not existed");
            return ResponseEntity.noContent().build();
        }
        ReferrerModel referrer = referrerService.getReferrerByPrac(decodedString);
        return ResponseEntity.ok(referrer);
    }


    @GetMapping("/ref/{refid}")
    public ResponseEntity<?> findByRefID(@PathVariable String refid) {
        ReferrerModel referrer;
        try {
            referrer = referrerService.getReferrerByRefID(refid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Not exist Referrer!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(referrer, HttpStatus.OK);
    }

    @GetMapping("/prefix/{prefix}")
    public ResponseEntity<ReferrerModel> findByPrefix(@PathVariable String prefix) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(prefix));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = referrerService.existsByPrefix(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("Prefix " + decodedString + " is not existed");
            return ResponseEntity.noContent().build();
        }
        ReferrerModel referrer = referrerService.getReferrerByPrefix(decodedString);
        return ResponseEntity.ok(referrer);
    }

    @GetMapping("/check/{prac}")
    public Boolean checkReferrer(@PathVariable String prac) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(prac));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = referrerService.existsByPrac(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("Practitioner" + decodedString + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/check/prefix/{prefix}")
    public Boolean checkReferrerByPrefix(@PathVariable String prefix) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(prefix));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = referrerService.existsByPrefix(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("Practitioner prefix" + decodedString + " is not existed");
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ReferrerModel> update(@PathVariable Long id, @Valid @RequestBody ReferrerModel referrer) {
        if (!referrerService.findById(id).isPresent()) {
            log.error("Referrer Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }
        ReferrerModel referrerUpdate = referrerService.getOne(id);
        referrerUpdate.setDegree(referrer.getDegree());
        referrerUpdate.setName(referrer.getName());
        referrerUpdate.setLastname(referrer.getLastname());
        referrerUpdate.setName_eng(referrer.getName_eng());
        referrerUpdate.setLastname_eng(referrer.getLastname_eng());
        referrerUpdate.setPrefix(referrer.getPrefix());
        referrerUpdate.setSex(referrer.getSex());
        referrerUpdate.setCenter_code(referrer.getCenter_code());
        referrerUpdate.setPractitioner_id(referrer.getPractitioner_id());
        return ResponseEntity.ok(referrerService.save(referrerUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!referrerService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        referrerService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    private static String Urldecoder(String name) {
        String newName = "";
        try {
            newName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newName;
    }
}
