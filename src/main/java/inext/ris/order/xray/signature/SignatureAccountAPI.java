package inext.ris.order.xray.signature;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/api/v1/signature")
@Slf4j
@RequiredArgsConstructor
public class SignatureAccountAPI {
    @Autowired
    SignatureAccountService signatureAccountService;

    @GetMapping
    public ResponseEntity<List<SignatureAccount>> findAll() {
        return ResponseEntity.ok(signatureAccountService.findAll());
    }

    @PostMapping
    public ResponseEntity<SignatureAccount> create(@Valid @RequestBody SignatureAccount signatureAccount) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = signatureAccountService.existsByUserHSM(signatureAccount.getUser_hsm()).compareTo(bi);
        if (res > 0) {
            log.error("USER_HSM " + signatureAccount.getUser_hsm() + " does existed");
            return ResponseEntity.badRequest().build();
        }
        signatureAccountService.save(signatureAccount);
        return new ResponseEntity(signatureAccountService.getSignatureAccountByHSM(signatureAccount.getUser_hsm()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignatureAccount> findById(@PathVariable Long id) {
        Optional<SignatureAccount> signatureAccount = signatureAccountService.findById(id);
        if (!signatureAccount.isPresent()) {
            log.error("SignatureAccount Id " + id + " does not existed");
           return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(signatureAccount.get());
    }

    @GetMapping("/userid/{userid}")
    public ResponseEntity<SignatureAccount> findBySignatureByID(@PathVariable String userid) {
        SignatureAccount signatureAccount = signatureAccountService.getSignatureAccountByID(userid);
        if (signatureAccount.getUser_code().isEmpty()) {
            log.error("SignatureAccount userid " + userid + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(signatureAccount);
    }

    @GetMapping("/check/userid/{userid}")
    public Boolean checkByUserId(@PathVariable String userid) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = signatureAccountService.existsByUserId(userid).compareTo(bi);
        if (res <= 0) {
            log.error("Check SignatureAccount with User ID " + userid + " is not existed");
            return false;
        }
        return true;
    }


    @GetMapping("/usercode/{usercode}")
    public ResponseEntity<SignatureAccount> findBySignatureCode(@PathVariable String usercode) {
        SignatureAccount signatureAccount = signatureAccountService.getSignatureAccountByCode(usercode);
        if (signatureAccount.getUser_code().isEmpty()) {
            log.error("SignatureAccount usercode " + usercode + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(signatureAccount);
    }

    @GetMapping("/check/usercode/{usercode}")
    public Boolean checkByUserCode(@PathVariable String usercode) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = signatureAccountService.existsByUserCode(usercode).compareTo(bi);
        if (res <= 0) {
            log.error("Check SignatureAccount with User Code " + usercode + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/userhsm/{userhsm}")
    public ResponseEntity<SignatureAccount> findBySignatureHsm(@PathVariable String userhsm) {
        SignatureAccount signatureAccount = signatureAccountService.getSignatureAccountByHSM(userhsm);
        if (signatureAccount.getUser_code().isEmpty()) {
            log.error("SignatureAccount userhsm " + userhsm + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(signatureAccount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<SignatureAccount> update(@PathVariable Long id, @Valid @RequestBody SignatureAccount signatureAccount) {
        if (!signatureAccountService.findById(id).isPresent()) {
            log.error("SignatureAccount Id " + id + " does not existed");
          return ResponseEntity.badRequest().build();
        }
        SignatureAccount signatureAccountUpdate = signatureAccountService.getOne(id);
        signatureAccountUpdate.setPass_hsm(signatureAccount.getPass_hsm());
        signatureAccountUpdate.setCts(signatureAccount.getCts());
        signatureAccountUpdate.setPin(signatureAccount.getPin());
        signatureAccountUpdate.setAvatar(signatureAccount.getAvatar());
        signatureAccountUpdate.setText_signature(signatureAccount.getText_signature());
        signatureAccountUpdate.setLast_sign(new Date());
        return ResponseEntity.ok(signatureAccountService.save(signatureAccountUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!signatureAccountService.findById(id).isPresent()) {
            log.error("SignatureAccount Id " + id + " does not existed");
           return ResponseEntity.badRequest().build();
        }
        signatureAccountService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
