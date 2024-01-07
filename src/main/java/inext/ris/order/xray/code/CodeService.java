package inext.ris.order.xray.code;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class CodeService {
	@Autowired
	CodeRepository codeRepository;
	
	public List<CodeModel> findAll() {
		return codeRepository.findAll();
	}
	
    public Optional<CodeModel> findById(Long id) {
    	return codeRepository.findById(id);
    }
    
    public CodeModel getOne(Long id) {
    	return codeRepository.getOne(id);
    }
    
    public BigInteger existsByXrayCode(String xraycode) {
    	return codeRepository.existsByXrayCode(xraycode);
    }
    
    public CodeModel getCodeByXrayCode (String xraycode) {
    	return codeRepository.getCodeByXrayCode(xraycode);
    }
    
    public List<CodeModel> getListCodeByXrayTypeCode (String xraytypecode) {
    	return codeRepository.getListCodeByXrayTypeCode(xraytypecode);
    }
    
    public BigInteger existsByDescription(String description, String modality) {
    	return codeRepository.existsByDescription(description, modality);
    }
    
    public BigInteger existsByXrayCode(String xraycode, String modality) {
    	return codeRepository.existsByXrayCode(xraycode, modality);
    }
    
    public CodeModel getCodeByDescription (String description, String modality) {
    	return codeRepository.getCodeByDescription(description, modality);
    }
    
    public CodeModel getCodeByXrayCodeE (String xraycode, String modality) {
    	return codeRepository.getCodeByXrayCodeE(xraycode, modality);
    }
    
    public CodeModel save(CodeModel code) {
        return codeRepository.save(code);
    }
       
    public void deleteById(Long id) {
    	codeRepository.deleteById(id);
    }
}
