package inext.ris.order.xray.delete;

import java.math.BigInteger;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/delete")
@Slf4j
@RequiredArgsConstructor
public class DeleteAPI {
	@Autowired
	DeleteService deleteService;
	
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DeleteModel deleteModel) {
    	BigInteger bi = new BigInteger("0");
    	int res;
    	res = deleteService.existsByBundleID(deleteModel.getBundle_id()).compareTo(bi);
        if (res > 0) {
            log.error("Bundle ID " + deleteModel.getBundle_id() + " is existed");
          return new ResponseEntity<String>("Patient exist!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(deleteService.save(deleteModel));
    }

    @GetMapping("/check/{bundle_id}")
    public Boolean checkRequestDetail(@PathVariable String bundle_id) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = deleteService.existsByBundleID(bundle_id).compareTo(bi);
        if (res <= 0) {
            log.error("Bundle ID " + bundle_id + " is not existed");
            return false;
        }
        return true;
    }
}
