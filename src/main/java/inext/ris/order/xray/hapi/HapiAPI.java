package inext.ris.order.xray.hapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/hapi")
@Slf4j
@RequiredArgsConstructor
public class HapiAPI {
    @Autowired
    HapiService hapiService;

    @GetMapping("/param")
    public ResponseEntity<Hapi> findById() {
        Hapi hapi = null;
        try {
            hapi = hapiService.getHapi();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(hapi);
    }
}
