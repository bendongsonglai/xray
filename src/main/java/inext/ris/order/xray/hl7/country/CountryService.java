package inext.ris.order.xray.hl7.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    @Autowired
    CountryRepository countryRepository;

    public Country FindCountryDefault () {
        return countryRepository.FindCountryDefault();
    }
}
