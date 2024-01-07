package inext.ris.order.xray.workstation;

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
@RequestMapping("/api/v1/ws")
@Slf4j
@RequiredArgsConstructor
public class WSAPI {
    @Autowired
    WSService wsService;
    @GetMapping("/type/{type_id}")
    public ResponseEntity<WSModel> findByTypeID(@PathVariable String type_id) {
        WSModel ws = wsService.getWorkstationByTypeID(Integer.parseInt(type_id));
        BigInteger bi = new BigInteger("0");
        int res;
        res = wsService.existsByTypeID(Integer.parseInt(type_id)).compareTo(bi);
        if (res <= 0) {
            log.error("Workstion with Type " + type_id + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ws);
    }

    @GetMapping("/aetitle/{aetitle}")
    public ResponseEntity<WSModel> findByTypeByAe(@PathVariable String aetitle) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = wsService.existsByAe(aetitle).compareTo(bi);
        if (res <= 0) {
            log.error("Workstion with Ae " + aetitle + " is not existed");
            return ResponseEntity.noContent().build();
        }
        WSModel ws = wsService.getWorkstationByAe(aetitle);
        return ResponseEntity.ok(ws);
    }
}
