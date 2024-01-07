package inext.ris.order.xray.hl7.race;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/terminology/race")
@Slf4j
@RequiredArgsConstructor
public class RaceAPI {
    @Autowired
    RaceService raceService;

    @GetMapping("/default")
    public ResponseEntity<?> findByDefault() {
        Race race;
        try {
            race  = raceService.FindRaceDefault();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Not exist default Race!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(race, HttpStatus.OK);
    }
}
