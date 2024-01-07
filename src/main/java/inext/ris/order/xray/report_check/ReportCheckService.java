package inext.ris.order.xray.report_check;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service

@RequiredArgsConstructor
public class ReportCheckService {
    @Autowired
    ReportCheckRepository reportCheckRepository;

    public BigInteger existsByAccNo(String acc) {
        return reportCheckRepository.existsByAccNo(acc);
    }

    public ReportCheck save(ReportCheck reportCheck) {
        return reportCheckRepository.save(reportCheck);
    }
}
