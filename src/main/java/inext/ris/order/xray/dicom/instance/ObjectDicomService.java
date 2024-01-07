package inext.ris.order.xray.dicom.instance;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ObjectDicomService {
    @Autowired
    ObjectDicomRepository objectDicomRepository;

    public List<ObjectDicom> findAll() {
        return objectDicomRepository.findAll();
    }

    public Optional<ObjectDicom> findById(Long id) {
        return objectDicomRepository.findById(id);
    }

    public ObjectDicom getOne(Long id) {
        return objectDicomRepository.getOne(id);
    }


    public BigInteger existsByObject(String object) {
        return objectDicomRepository.existsByObject(object);
    }

    public List<ObjectDicom> listDICOMBySeries(String series) {
        return objectDicomRepository.listDICOMBySeries(series);
    }

    public ObjectDicom save(ObjectDicom object) {
        return objectDicomRepository.save(object);
    }
}
