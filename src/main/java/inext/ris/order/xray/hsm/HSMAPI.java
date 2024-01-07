package inext.ris.order.xray.hsm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/hsm")
@Slf4j
@RequiredArgsConstructor
public class HSMAPI {
    @Autowired
    HSMService hsmService;

    @GetMapping
    public ResponseEntity<List<HSMModel>> findAll() {
        return ResponseEntity.ok(hsmService.findAll());
    }

    @PostMapping
    public ResponseEntity<HSMModel> create(@Valid @RequestBody HSMModel hsmModel) {
        try {
            hsmService.save(hsmModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(hsmService.save(hsmModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HSMModel> findById(@PathVariable Long id) {
        Optional<HSMModel> hsm = hsmService.findById(id);
        if (!hsm.isPresent()) {
            log.error("HSM Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(hsm.get());
    }
}
