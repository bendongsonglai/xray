package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.LocationConvert;
import inext.ris.order.xray.his.model.LocationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/location/convert")
@RequiredArgsConstructor
public class LocationConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody LocationData locationData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        LocationConvert locationConvert = new LocationConvert();
        Location location;
        String locationSerialized;
        try {
            location = locationConvert.locationHospitalFHIR(locationData);
            locationSerialized = parser.encodeResourceToString(location);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Location error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(locationSerialized, HttpStatus.CREATED);
    }

    @GetMapping("/check/{name}")
    public Boolean checkDepartmentCode(@PathVariable String name) {
        LocationConvert locationConvert = new LocationConvert();
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String code = Base64.getEncoder().encodeToString(decodedBytes);
        try {
            return locationConvert.checkLocationByCode(code);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Check Location error!");
            return true;
        }
    }

    private static String Urldecoder (String name) {
        String newName = "";
        try {
            newName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newName;
    }
}
