package inext.ris.order.xray.hl7.msh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/msh")
@Slf4j
@RequiredArgsConstructor
public class MSHAPI {
    @Autowired
    MSHService mshService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<MSH> msh = mshService.findById(id);
        if (!msh.isPresent()) {
            log.error("Id " + id + " is not existed");
            return new ResponseEntity<String>("Not exist MSH data!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<MSH>(msh.get(), HttpStatus.OK);
    }
}
