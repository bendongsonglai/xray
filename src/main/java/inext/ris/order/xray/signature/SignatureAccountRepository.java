package inext.ris.order.xray.signature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface SignatureAccountRepository extends JpaRepository<SignatureAccount, Long> {
    @Query(value="SELECT COUNT(id) > 0 FROM xray_signature_account WHERE USER_ID = :userid", nativeQuery = true)
    BigInteger existsByUserId(@Param("userid") String userid);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_signature_account WHERE USER_CODE = :usercode", nativeQuery = true)
    BigInteger existsByUserCode(@Param("usercode") String usercode);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_signature_account WHERE USER_HSM = :userhsm", nativeQuery = true)
    BigInteger existsByUserHSM(@Param("userhsm") String userhsm);

    @Query(value="SELECT * FROM xray_signature_account WHERE USER_ID = :userid", nativeQuery = true)
    SignatureAccount getSignatureAccountByID(@Param("userid") String userid);

    @Query(value="SELECT * FROM xray_signature_account WHERE USER_CODE = :usercode", nativeQuery = true)
    SignatureAccount getSignatureAccountByCode(@Param("usercode") String usercode);

    @Query(value="SELECT * FROM xray_signature_account WHERE USER_HSM = :userhsm", nativeQuery = true)
    SignatureAccount getSignatureAccountByHSM(@Param("userhsm") String userhsm);
}
