package inext.ris.order.xray.aes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/aes")
@Slf4j
@RequiredArgsConstructor
public class AESAPI {
    @Autowired
    AESService aesService;

    @GetMapping("/param")
    public ResponseEntity<AES> findById() {
        AES aes = null;
        try {
            aes = aesService.getAes();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aes);
    }
}
