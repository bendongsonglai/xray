package inext.ris.order.xray.dicom.series;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeriesService {
    @Autowired
    SeriesRepository seriesRepository;

    public List<SeriesModel> findAll() {
        return seriesRepository.findAll();
    }

    public Optional<SeriesModel> findById(Long id) {
        return seriesRepository.findById(id);
    }

    public SeriesModel getOne(Long id) {
        return seriesRepository.getOne(id);
    }


    public BigInteger existsBySeries(String series) {
        return seriesRepository.existsBySeries(series);
    }

    public List<SeriesModel> listDICOMByStudy(String study) {
        return seriesRepository.listDICOMByStudy(study);
    }

    public SeriesModel save(SeriesModel series) {
        return seriesRepository.save(series);
    }
}
