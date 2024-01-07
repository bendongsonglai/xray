package inext.ris.order.xray.reportsigned;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/signaturepath")
@Slf4j
@RequiredArgsConstructor
public class SignatureAPI {
    @Autowired
    SignatureService signatureService;

    @GetMapping
    public ResponseEntity<List<Signature>> findAll() {
        return ResponseEntity.ok(signatureService.findAll());
    }

    @PostMapping
    public ResponseEntity<Signature> create(@Valid @RequestBody Signature signature) {
        return ResponseEntity.ok(signatureService.save(signature));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Signature> findById(@PathVariable Long id) {
        Optional<Signature> signatureAccount = signatureService.findById(id);
        if (!signatureAccount.isPresent()) {
            log.error("SignaturePath Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(signatureAccount.get());
    }

}
