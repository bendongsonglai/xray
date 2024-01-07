package inext.ris.order.xray.hislog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/hislog")
@Slf4j
@RequiredArgsConstructor
public class HisLogAPI {
    @Autowired
    HisLogService hisLogService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody HisLog hisLog) {
        return ResponseEntity.ok(hisLogService.save(hisLog));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HisLog> findById(@PathVariable Long id) {
        Optional<HisLog> fhir = hisLogService.findById(id);
        if (!fhir.isPresent()) {
            log.error("Id " + id + " is not existed");
           return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fhir.get());
    }
}
