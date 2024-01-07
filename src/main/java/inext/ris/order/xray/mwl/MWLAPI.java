package inext.ris.order.xray.mwl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mwl")
@Slf4j
@RequiredArgsConstructor
public class MWLAPI {
    @Autowired
    MWLServices mwlServices;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody MWLModel mwl) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = mwlServices.existsByACC(mwl.getAccession_number()).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION_NUMBER " + mwl.getAccession_number() + " is existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mwlServices.save(mwl));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MWLModel> findById(@PathVariable Long id) {
        Optional<MWLModel> mwl = mwlServices.findById(id);
        if (!mwl.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mwl.get());
    }

    @GetMapping("/acc/{acc}")
    public ResponseEntity<MWLModel> findByAcc(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = mwlServices.existsByACC(acc).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION_NUMBER " + acc + " is existed");
            ResponseEntity.badRequest().build();
        }
        MWLModel mwl = mwlServices.getMWLByAcc(acc);
        return ResponseEntity.ok(mwl);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MWLModel>> ListMWL(@PathVariable String status) {
        List<MWLModel> mwls = null;
        try {
            mwls = mwlServices.ListsMWL(status);
        } catch (Exception e) {
            log.error("List MWL with status {} error!", status);
            e.printStackTrace();
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mwls);
    }

    @RequestMapping(value = "/setstatus/{acc}", method = RequestMethod.POST)
    public ResponseEntity<MWLModel> update(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = mwlServices.existsByACC(acc).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION_NUMBER " + acc + " is existed");
            ResponseEntity.badRequest().build();
        }
        MWLModel mwlUpdate = mwlServices.getMWLByAcc(acc);
        mwlUpdate.setStatus("1");
        return ResponseEntity.ok(mwlServices.save(mwlUpdate));
    }

    @GetMapping("/check/{acc}")
    public Boolean checkBundle(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = mwlServices.existsByACC(acc).compareTo(bi);
        if (res <= 0) {
            log.error("ACCESSION_NUMBER " + acc + " is not existed");
            return false;
        }
        return true;
    }
}
