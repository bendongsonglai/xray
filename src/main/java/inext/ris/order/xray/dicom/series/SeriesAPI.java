package inext.ris.order.xray.dicom.series;

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
@RequestMapping("/api/v1/series")
@Slf4j
@RequiredArgsConstructor
public class SeriesAPI {
    @Autowired
    SeriesService seriesService;

    @GetMapping
    public ResponseEntity<List<SeriesModel>> findAll() {
        return ResponseEntity.ok(seriesService.findAll());
    }

    @PostMapping
    public ResponseEntity<SeriesModel> create(@Valid @RequestBody SeriesModel seriesModel) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = seriesService.existsBySeries(seriesModel.getSeries()).compareTo(bi);
        if (res > 0) {
            log.error("Series " + seriesModel.getSeries() + " does existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(seriesService.save(seriesModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeriesModel> findById(@PathVariable Long id) {
        Optional<SeriesModel> request = seriesService.findById(id);
        if (!request.isPresent()) {
            log.error("DICOM Series Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(request.get());
    }

    @GetMapping("/study")
    public ResponseEntity<List<SeriesModel>> findByStudy(@RequestParam(required = false) String studyuid) {
        List<SeriesModel> seriesModels = seriesService.listDICOMByStudy(studyuid);
        return ResponseEntity.ok(seriesModels);
    }
}
