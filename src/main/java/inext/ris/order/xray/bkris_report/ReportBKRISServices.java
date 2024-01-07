package inext.ris.order.xray.bkris_report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class ReportBKRISServices {
    @Autowired
    ReportBKRISRepository reportBKRISRepository;

    public Optional<ReportBKRIS> findById(Long id) {
        return reportBKRISRepository.findById(id);
    }

    public ReportBKRIS getOne(Long id) {
        return reportBKRISRepository.getOne(id);
    }


    public BigInteger existsByACC(String acc) {
        return reportBKRISRepository.existsByACC(acc);
    }

    public ReportBKRIS save(ReportBKRIS report) {
        return reportBKRISRepository.save(report);
    }

    public void deleteById(Long id) {
        reportBKRISRepository.deleteById(id);
    }
}
