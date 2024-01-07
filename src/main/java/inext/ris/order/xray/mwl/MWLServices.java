package inext.ris.order.xray.mwl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class MWLServices {
    @Autowired
    MWLRepository mwlRepository;

    public Optional<MWLModel> findById(Long id) {
        return mwlRepository.findById(id);
    }

    public MWLModel getOne(Long id) {
        return mwlRepository.getOne(id);
    }

    public BigInteger existsByACC(String acc) {
        return mwlRepository.existsByACC(acc);
    }

    public void updateMWLByAcc(String status, String acc) {
        mwlRepository.updateMWLByAcc(status, acc);
    }

    public List<MWLModel> ListsMWL(String status) {
      return mwlRepository.ListsMWL(status);
    }

    public MWLModel getMWLByAcc(String acc){
        return mwlRepository.getMWLByAcc(acc);
    }

    public MWLModel save(MWLModel mwl) {
        return mwlRepository.save(mwl);
    }

    public void deleteById(Long id) {
        mwlRepository.deleteById(id);
    }
}
