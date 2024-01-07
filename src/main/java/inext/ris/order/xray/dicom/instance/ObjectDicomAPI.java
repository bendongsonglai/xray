package inext.ris.order.xray.dicom.instance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/object")
@Slf4j
@RequiredArgsConstructor
public class ObjectDicomAPI {
    @Autowired
    ObjectDicomService objectDicomService;

    @GetMapping
    public ResponseEntity<List<ObjectDicom>> findAll() {
        return ResponseEntity.ok(objectDicomService.findAll());
    }

    @PostMapping
    public ResponseEntity<ObjectDicom> create(@Valid @RequestBody ObjectDicom objectDicom) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = objectDicomService.existsByObject(objectDicom.getObject()).compareTo(bi);
        if (res > 0) {
            log.error("Object " + objectDicom.getObject() + " does existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(objectDicomService.save(objectDicom));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectDicom> findById(@PathVariable Long id) {
        Optional<ObjectDicom> request = objectDicomService.findById(id);
        if (!request.isPresent()) {
            log.error("DICOM Object Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(request.get());
    }

    @GetMapping("/series")
    public ResponseEntity<List<ObjectDicom>> findByStudy(@RequestParam(required = false) String seriesuid) {
        List<ObjectDicom> seriesModels = objectDicomService.listDICOMBySeries(seriesuid);
        return ResponseEntity.ok(seriesModels);
    }
}
