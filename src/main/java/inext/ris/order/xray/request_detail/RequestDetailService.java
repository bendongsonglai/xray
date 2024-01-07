package inext.ris.order.xray.request_detail;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class RequestDetailService {
	@Autowired
	RequestDetailRepository requestDetailRepository;
	
	public List<RequestDetailModel> findAll() {
		return requestDetailRepository.findAll();
	}
	
    public Optional<RequestDetailModel> findById(Long id) {
    	return requestDetailRepository.findById(id);
    }
    
    public RequestDetailModel getOne(Long id) {
    	return requestDetailRepository.getOne(id);
    }
    
    public BigInteger existsByAccession(String acc) {
    	return requestDetailRepository.existsByAccNo(acc);
    }

    public BigInteger existsByAccNoAndImage(String acc) {
        return requestDetailRepository.existsByAccNoAndImage(acc);
    }
    
    public RequestDetailModel getRequestDetailByAccesion (String accNo) {
    	return requestDetailRepository.getRequestDetailByAccession(accNo);
    }
    
    public void  updateRequestDetailByAccession (String status, String page, String tech1, String assign_by, String start_time, String assign_time, String accNo) {
    	requestDetailRepository.updateRequestDetailByAccession(status, page, tech1, assign_by, start_time, assign_time, accNo);
    }

    public void  updateToReportByAccession (String status, String page, String assign, String approved_time, String temp_report, String accNo) {
        requestDetailRepository.updateToReportByAccession(status, page, assign, approved_time, temp_report, accNo);
    }

    public void updateWorkstation (String workstation, String accNo) {
        requestDetailRepository.updateWorkstation(workstation, accNo);
    }

    public void updateStudy (String totalInstances, String status, Date arrival_time, String accNo) {
        requestDetailRepository.updateStudy(totalInstances, status, arrival_time, accNo);
    }

    public void updateOrderControl (String ctr, String accNo) {
        requestDetailRepository.updateOrderControl(ctr, accNo);
    }

    public void updateOrderControlNormal (String ctr, String accNo) {
        requestDetailRepository.updateOrderControlNormal(ctr, accNo);
    }

    public RequestDetailModel save(RequestDetailModel requestDetail) {
        return requestDetailRepository.save(requestDetail);
    }
       
    public void deleteById(Long id) {
    	requestDetailRepository.deleteById(id);
    }
}
