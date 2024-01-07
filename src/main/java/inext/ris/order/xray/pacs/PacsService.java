package inext.ris.order.xray.pacs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacsService {
    @Autowired
    PacsRepository pacsRepository;

    public List<PacsModel> findAll() {
        return pacsRepository.findAll();
    }

    public Optional<PacsModel> findById(Long id) {
        return pacsRepository.findById(id);
    }

    public PacsModel getOne(Long id) {
        return pacsRepository.getOne(id);
    }

    public BigInteger existsByAetitle(String aetitle) {
        return pacsRepository.existsByAetitle(aetitle);
    }

    public PacsModel save(PacsModel pacs) {
        return pacsRepository.save(pacs);
    }

    public void deleteById(Long id) {
        pacsRepository.deleteById(id);
    }
}
