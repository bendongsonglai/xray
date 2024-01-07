package inext.ris.order.xray.hl7.race;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RaceService {
    @Autowired
    RaceRepository raceRepository;

    public Race FindRaceDefault () {
        return raceRepository.FindRaceDefault();
    }
}
