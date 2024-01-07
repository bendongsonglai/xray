package inext.ris.order.xray.majorProblem;

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
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/majorproblem")
@Slf4j
@RequiredArgsConstructor
public class MajorProblemAPI {
    @Autowired
    MajorProblemService majorProblemService;

    @GetMapping
    public ResponseEntity<List<MajorProblem>> findAll() {
        return ResponseEntity.ok(majorProblemService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody MajorProblem majorProblem) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = majorProblemService.existsByName(majorProblem.getName()).compareTo(bi);
        if (res > 0) {
            log.error("Name " + majorProblem.getName() + " does existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(majorProblemService.save(majorProblem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorProblem> findById(@PathVariable Long id) {
        Optional<MajorProblem> clinicalElement = majorProblemService.findById(id);
        if (!clinicalElement.isPresent()) {
            log.error("Clinical Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clinicalElement.get());
    }

    @GetMapping("/all")
    public ResponseEntity<List<MajorProblem>> majorProblems() {
        List<MajorProblem> majorProblems = majorProblemService.clinicalElementsEnabled();
        return ResponseEntity.ok(majorProblems);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MajorProblem> findByAccession(@PathVariable String name) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String decodedString = new String(decodedBytes);
        MajorProblem clinicalElement = majorProblemService.majorProblemByName(decodedString);
        if (clinicalElement.getName().isEmpty()) {
            log.error("Major Problem name " + decodedString + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clinicalElement);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MajorProblem> update(@PathVariable Long id, @Valid @RequestBody MajorProblem majorProblem) {
        if (!majorProblemService.findById(id).isPresent()) {
            log.error("Major Problem Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        MajorProblem majorProblemUpdate= majorProblemService.getOne(id);
        majorProblemUpdate.setValue(majorProblem.getValue());
        majorProblemUpdate.setEnabled(majorProblem.getEnabled());
        return ResponseEntity.ok(majorProblemService.save(majorProblemUpdate));
    }

    @DeleteMapping("/remove/{name}")
    public ResponseEntity<String> remove(@PathVariable String name) {
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = majorProblemService.existsByName(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("Name " + decodedString + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        majorProblemService.removeMajorProblemByName(decodedString);
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!majorProblemService.findById(id).isPresent()) {
            log.error("Clinical Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }

        majorProblemService.deleteById(id);

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
