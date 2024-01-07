package inext.ris.order.xray.dicom;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DicomService {
    @Autowired
    DicomRepository dicomRepository;

    public List<DicomModel> findAll() {
        return dicomRepository.findAll();
    }

    public Optional<DicomModel> findById(Long id) {
        return dicomRepository.findById(id);
    }

    public DicomModel getOne(Long id) {
        return dicomRepository.getOne(id);
    }

    public BigInteger existsByAccNo(String acc) {
        return dicomRepository.existsByAccNo(acc);
    }

    public DicomModel getDICOMByAcc(String acc) {
        return dicomRepository.getDICOMByAcc(acc);
    }

    public DicomModel getDICOMByStudy(String study) {
        return dicomRepository.getDICOMByStudy(study);
    }

    public List<DicomModel> getList(String from, String to) {
        return dicomRepository.getList(from, to);
    }

    public DicomModel save(DicomModel patient) {
        return dicomRepository.save(patient);
    }

    public void deleteById(Long id) {
        dicomRepository.deleteById(id);
    }
}
