package inext.ris.order.xray.hl7.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/terminology/country")
@Slf4j
@RequiredArgsConstructor
public class CountryAPI {
    @Autowired
    CountryService countryService;

    @GetMapping("/default")
    public ResponseEntity<?> findByDefault() {
        Country country;
        try {
            country  = countryService.FindCountryDefault();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Not exist default Country!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
}
