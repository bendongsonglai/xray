package inext.ris.order.xray.doituong;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/doituong")
@Slf4j
@RequiredArgsConstructor
public class DoituongAPI {
    @Autowired
    DoituongService doituongService;

    @GetMapping("/type/{type_id}")
    public ResponseEntity<DoituongModel> findByTypeID(@PathVariable String type_id) {
        DoituongModel dt = doituongService.getDoituongByTypeID(type_id);
        BigInteger bi = new BigInteger("0");
        int res;
        res = doituongService.existsByTypeID(type_id).compareTo(bi);
        if (res <= 0) {
            log.error("Doituong with Type " + type_id + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dt);
    }
}
