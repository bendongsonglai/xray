package inext.ris.order.xray.bkris_report;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportbkris")
@Slf4j
@RequiredArgsConstructor
public class ReportBKRISAPI {
    @Autowired
    ReportBKRISServices reportBKRISServices;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ReportBKRIS report) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportBKRISServices.existsByACC(report.getAccession()).compareTo(bi);
        if (res > 0) {
            log.error("Report with acc " + report.getAccession() + " is existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reportBKRISServices.save(report));
    }

    @GetMapping("/check/{acc}")
    public Boolean checkBundle(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportBKRISServices.existsByACC(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Report with acc " + acc + " is not existed");
            return false;
        }
        return true;
    }
}
