package inext.ris.order.xray.dicom;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dicom")
@Slf4j
@RequiredArgsConstructor
public class DicomAPI {
    @Autowired
    DicomService dicomService;

    @GetMapping
    public ResponseEntity<List<DicomModel>> findAll() {
        return ResponseEntity.ok(dicomService.findAll());
    }

    @PostMapping
    public ResponseEntity<DicomModel> create(@Valid @RequestBody DicomModel dicom) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = dicomService.existsByAccNo(dicom.getAccession()).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION " + dicom.getAccession() + " does existed");
           return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dicomService.save(dicom));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DicomModel> findById(@PathVariable Long id) {
        Optional<DicomModel> request = dicomService.findById(id);
        if (!request.isPresent()) {
            log.error("Request Id " + id + " does not existed");
           return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(request.get());
    }

    @GetMapping("/accession")
    public ResponseEntity<DicomModel> findByAccession(@RequestParam(required = false) String acc) {
        DicomModel dicom = dicomService.getDICOMByAcc(acc);
        if (dicom.getAccession().isEmpty()) {
            log.error("Dicom ACCESSION " + acc + " does not existed");
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dicom);
    }

    @GetMapping("/study")
    public ResponseEntity<DicomModel> findByStudy(@RequestParam(required = false) String studyuid) {
        DicomModel dicom = dicomService.getDICOMByStudy(studyuid);
        if (dicom.getStudy().isEmpty()) {
            log.error("Dicom Study " + studyuid + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dicom);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DicomModel>> findByList(@RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        List<DicomModel> dicoms = dicomService.getList(from, to);
        if (dicoms.size() == 0) {
            log.error("Dicom List = 0");
        }
        return new ResponseEntity<>(dicoms, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!dicomService.findById(id).isPresent()) {
            log.error("Request Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        dicomService.deleteById(id);
        return new ResponseEntity<>("Deleted!", HttpStatus.ACCEPTED);
    }
}
