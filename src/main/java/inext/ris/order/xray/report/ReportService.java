package inext.ris.order.xray.report;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class ReportService {
	@Autowired
	ReportRepository reportRepository;
	
	public List<ReportModel> findAll() {
		return reportRepository.findAll();
	}
	
    public Optional<ReportModel> findById(Long id) {
    	return reportRepository.findById(id);
    }
    
    public BigInteger existsByAccession(String acc) {
    	return reportRepository.existsByAccession(acc);
    }
    
    public ReportModel getReportByAcc(String acc) {
    	return reportRepository.getReportByAcc(acc);
    }
    
    public ReportModel getOne(Long id) {
    	return reportRepository.getOne(id);
    }
    
    public ReportModel save(ReportModel report) {
        return reportRepository.save(report);
    }
    
    public void  updateReportByAccession (String dictate_by, String approve_by, String describereport, String conclusion, Date date, Date time, String accNo) {
    	reportRepository.updateReportByAccession(dictate_by, approve_by, describereport, conclusion, date, time, accNo);
    }

    public void  deleteReportByAccession (String newAcc, String accNo) {
        reportRepository.deleteReportByAccession(newAcc, accNo);
    }

    public void deleteById(Long id) {
    	reportRepository.deleteById(id);
    }
}
