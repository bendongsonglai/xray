package inext.ris.order.xray.request;

import java.math.BigInteger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class RequestService {
	@Autowired
	RequestRepository requestRepository;
	
	public List<RequestModel> findAll() {
		return requestRepository.findAll();
	}
	
    public Optional<RequestModel> findById(Long id) {
    	return requestRepository.findById(id);
    }
    
    public RequestModel getOne(Long id) {
    	return requestRepository.getOne(id);
    }
    
    public BigInteger existsByMRN(String acc) {
    	return requestRepository.existsByAccNo(acc);
    }

    public BigInteger existsByAccNoAndMrn(String mrn, String acc) {
        return requestRepository.existsByAccNoAndMrn(mrn, acc);
    }

    public RequestModel getRequestByAcc(String acc) {
        return requestRepository.getRequestByAcc(acc);
    }

    public List<String> listStudy(Date from, Date  to) {
        return requestRepository.listStudy(from, to);
    }
    
    public RequestModel save(RequestModel patient) {
        return requestRepository.save(patient);
    }
       
    public void deleteById(Long id) {
    	requestRepository.deleteById(id);
    }
}
