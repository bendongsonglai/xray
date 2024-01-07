package inext.ris.order.xray.clinicalElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicalElementService {
    @Autowired
    ClinicalElementRepository clinicalElementRepository;

    public List<ClinicalElement> findAll() {
        return clinicalElementRepository.findAll();
    }

    public Optional<ClinicalElement> findById(Long id) {
        return clinicalElementRepository.findById(id);
    }

    public ClinicalElement getOne(Long id) {
        return clinicalElementRepository.getOne(id);
    }

    public BigInteger existsByName(String name) {
        return clinicalElementRepository.existsByName(name);
    }

    public ClinicalElement clinicalElementByName(String name) {
        return clinicalElementRepository.clinicalElementByName(name);
    }

    public List<ClinicalElement> clinicalElementsEnabled() {
        return clinicalElementRepository.clinicalElementsEnabled();
    }

    public void removeClinicalElementByName(String name) {
        clinicalElementRepository.removeClinicalElementByName(name);
    }

    public ClinicalElement save(ClinicalElement clinicalElement) {
        return clinicalElementRepository.save(clinicalElement);
    }

    public void deleteById(Long id) {
        clinicalElementRepository.deleteById(id);
    }
}
