package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.PractitionerConvert;
import inext.ris.order.xray.his.model.PractitionerData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/practitioner/convert")
@RequiredArgsConstructor
public class PractitionerConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody PractitionerData practitionerData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        PractitionerConvert practitionerConvert = new PractitionerConvert();
        Practitioner practitioner;
        String practitionerSerialized;
        try {
            practitioner = practitionerConvert.PractitionerFHIR(practitionerData);
            practitionerSerialized = parser.encodeResourceToString(practitioner);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Practitioner error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(practitionerSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> check(@RequestParam String mabs) {
        Boolean check = true;
        PractitionerConvert practitionerConvert = new PractitionerConvert();
        try {
            check = practitionerConvert.checkPractitionerByMabs(mabs);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Check Practitioner error!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(check);
    }
}
