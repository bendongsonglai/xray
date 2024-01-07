package inext.ris.order.xray.code;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/api/v1/code")
@Slf4j
@RequiredArgsConstructor
public class CodeAPI {
    @Autowired
    CodeService codeService;

    @GetMapping
    public ResponseEntity<List<CodeModel>> findAll() {
        return ResponseEntity.ok(codeService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CodeModel code) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = codeService.existsByXrayCode(code.getXray_code()).compareTo(bi);
        if (res > 0) {
            log.error("XRAY_CODE " + code.getXray_code() + " does existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(codeService.save(code));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodeModel> findById(@PathVariable Long id) {
        Optional<CodeModel> code = codeService.findById(id);
        if (!code.isPresent()) {
            log.error("Code Id " + id + " does not existed");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(code.get());
    }

    @RequestMapping(value = "/normal/{xraycode}", method = RequestMethod.GET)
    public ResponseEntity<CodeModel> findByXrayCode(@PathVariable String xraycode) {
        CodeModel code = codeService.getCodeByXrayCode(xraycode);
        if (code.getXray_code().isEmpty()) {
            log.error("Code XRAY_CODE " + xraycode + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(code);
    }

    @RequestMapping(value = "/list/{xraytypecode}", method = RequestMethod.GET)
    public ResponseEntity<List<CodeModel>> findListByXrayTypeCode(@PathVariable String xraytypecode) {
        List<CodeModel> code = codeService.getListCodeByXrayTypeCode(xraytypecode);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/check/{xraycode}")
    public Boolean checkCode(@PathVariable String xraycode) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = codeService.existsByXrayCode(xraycode).compareTo(bi);
        if (res <= 0) {
            log.error("Code " + xraycode + " is not existed");
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/description/{description}/{modality}", method = RequestMethod.GET)
    public ResponseEntity<CodeModel> getCodeByDescription(@PathVariable String description, @PathVariable String modality) {
        log.info("Descriptiton encode: {}", description);
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(description));
        String decodedString = new String(decodedBytes);
        CodeModel code = codeService.getCodeByDescription(decodedString, modality);
        BigInteger bi = new BigInteger("0");
        int res;
        res = codeService.existsByDescription(decodedString, modality).compareTo(bi);
        if (res <= 0) {
            log.error("DESCRIPTION " + decodedString + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(code);
    }

    @RequestMapping(value = "/xraycode/{xraycode}/{modality}", method = RequestMethod.GET)
    public ResponseEntity<CodeModel> getCodeByXrayCode(@PathVariable String xraycode, @PathVariable String modality) {
        CodeModel code = codeService.getCodeByXrayCodeE(xraycode, modality);
        BigInteger bi = new BigInteger("0");
        int res;
        res = codeService.existsByXrayCode(xraycode, modality).compareTo(bi);
        if (res <= 0) {
            log.error("XRAY_CODE " + xraycode + " is not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(code);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CodeModel> update(@PathVariable Long id, @Valid @RequestBody CodeModel code) {
        if (!codeService.findById(id).isPresent()) {
            log.error("Code Id " + id + " does not existed");
            return ResponseEntity.notFound().build();
        }
        CodeModel codeUpdate = codeService.getOne(id);
        codeUpdate.setCenter(code.getCenter());
        codeUpdate.setDescription(code.getDescription());
        codeUpdate.setXray_type_code(code.getXray_type_code());
        codeUpdate.setBody_part(code.getBody_part());
        codeUpdate.setCharge_total(code.getCharge_total());
        codeUpdate.setPortable_charge(code.getPortable_charge());
        codeUpdate.setDf(code.getDf());
        codeUpdate.setTime_use(code.getTime_use());
        codeUpdate.setBirad_flag(code.getBirad_flag());
        codeUpdate.setPrep_id(code.getPrep_id());
        codeUpdate.setDelete_flag(code.getDelete_flag());
        return ResponseEntity.ok(codeService.save(codeUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!codeService.findById(id).isPresent()) {
            log.error("Code Id " + id + " does not existed");
            return new ResponseEntity("Lỗi không tìm thấy dịch vụ theo id.", HttpStatus.NOT_FOUND);
        }
        codeService.deleteById(id);
        return new ResponseEntity<>("Xóa dịch vụ thành công.", HttpStatus.OK);
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

    public static void main(String[] args) {
        String name = "Q2jhu6VwIGPhuq90IGzhu5twIHZpIHTDrW5oIGLhu6VuZy10aeG7g3Uga2h1bmcgdGjGsOG7nW5nIHF1eSAodOG7qyAxLTMyIGTDo3k=";
        String newName = "";
        try {
            newName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] decodedBytes = Base64.getDecoder().decode(newName);
        String decodedString = new String(decodedBytes);
        System.out.println(decodedString);
    }
}
