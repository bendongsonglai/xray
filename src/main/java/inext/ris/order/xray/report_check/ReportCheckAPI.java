package inext.ris.order.xray.report_check;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/reportcheck")
@Slf4j
@RequiredArgsConstructor
public class ReportCheckAPI {
    @Autowired
    ReportCheckService reportCheckService;

    @PostMapping
    public ResponseEntity<ReportCheck> create(@Valid @RequestBody ReportCheck reportCheck) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportCheckService.existsByAccNo(reportCheck.getAccession()).compareTo(bi);
        if (res > 0) {
            log.error("Accession " + reportCheck.getAccession() + " does existed");
            ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(reportCheckService.save(reportCheck), HttpStatus.CREATED);
    }

    @GetMapping("/check/{acc}")
    public Boolean checkBundle(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = reportCheckService.existsByAccNo(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Report with acc " + acc + " is not existed");
            return false;
        }
        return true;
    }
}
