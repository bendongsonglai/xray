package inext.ris.order.xray.signature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class SignatureAccountService {
    @Autowired
    SignatureAccountRepository signatureAccountRepository;

    public List<SignatureAccount> findAll() {
        return signatureAccountRepository.findAll();
    }

    public Optional<SignatureAccount> findById(Long id) {
        return signatureAccountRepository.findById(id);
    }

    public SignatureAccount getOne(Long id) {
        return signatureAccountRepository.getOne(id);
    }

    public BigInteger existsByUserId(String userid) {
        return signatureAccountRepository.existsByUserId(userid);
    }

    public BigInteger existsByUserCode(String usercode) {
        return signatureAccountRepository.existsByUserCode(usercode);
    }

    public BigInteger existsByUserHSM(String userhsm) {
        return signatureAccountRepository.existsByUserHSM(userhsm);
    }

    public SignatureAccount getSignatureAccountByHSM(String userhsm) {
        return signatureAccountRepository.getSignatureAccountByHSM(userhsm);
    }

    public SignatureAccount getSignatureAccountByCode(String usercode) {
        return signatureAccountRepository.getSignatureAccountByCode(usercode);
    }

    public SignatureAccount getSignatureAccountByID(String userid) {
        return signatureAccountRepository.getSignatureAccountByID(userid);
    }
    public SignatureAccount save(SignatureAccount signature) {
        return signatureAccountRepository.save(signature);
    }

    public void deleteById(Long id) {
        signatureAccountRepository.deleteById(id);
    }
}
