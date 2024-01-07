package inext.ris.order.xray.clinicalElement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/v1/clinicalelement")
@Slf4j
@RequiredArgsConstructor
public class ClinicalElementAPI {
    @Autowired
    ClinicalElementService clinicalElementService;

    @GetMapping
    public ResponseEntity<List<ClinicalElement>> findAll() {
        return ResponseEntity.ok(clinicalElementService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ClinicalElement clinicalElement) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = clinicalElementService.existsByName(clinicalElement.getName()).compareTo(bi);
        if (res > 0) {
            log.error("Name " + clinicalElement.getName() + " does existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clinicalElementService.save(clinicalElement));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalElement> findById(@PathVariable Long id) {
        Optional<ClinicalElement> clinicalElement = clinicalElementService.findById(id);
        if (!clinicalElement.isPresent()) {
            log.error("Clinical Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clinicalElement.get());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClinicalElement>> clinicalElements() {
        List<ClinicalElement> clinicalElements = clinicalElementService.clinicalElementsEnabled();
        return ResponseEntity.ok(clinicalElements);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClinicalElement> findByAccession(@PathVariable String name) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String decodedString = new String(decodedBytes);
        ClinicalElement clinicalElement = clinicalElementService.clinicalElementByName(decodedString);
        if (clinicalElement.getName().isEmpty()) {
            log.error("Clinical name " + decodedString + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clinicalElement);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ClinicalElement> update(@PathVariable Long id, @Valid @RequestBody ClinicalElement clinicalElement) {
        if (!clinicalElementService.findById(id).isPresent()) {
            log.error("Clinical Id " + id + " does not existed");
          return ResponseEntity.badRequest().build();
        }
        ClinicalElement clinicalElementUpdate= clinicalElementService.getOne(id);
        clinicalElementUpdate.setDescription(clinicalElement.getDescription());
        clinicalElementUpdate.setDefault_val(clinicalElement.getDefault_val());
        clinicalElementUpdate.setUnit(clinicalElement.getUnit());
        clinicalElementUpdate.setEnabled(clinicalElement.getEnabled());
        return ResponseEntity.ok(clinicalElementService.save(clinicalElementUpdate));
    }

    @DeleteMapping("/remove/{name}")
    public ResponseEntity<String> remove(@PathVariable String name) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = clinicalElementService.existsByName(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("Name " + decodedString + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        clinicalElementService.removeClinicalElementByName(decodedString);
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!clinicalElementService.findById(id).isPresent()) {
            log.error("Clinical Id " + id + " does not existed");
          return ResponseEntity.badRequest().build();
        }

        clinicalElementService.deleteById(id);

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
