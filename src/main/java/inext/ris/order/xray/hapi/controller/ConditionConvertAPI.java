package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.ConditionConvert;
import inext.ris.order.xray.hapi.model.Comorbidity;
import inext.ris.order.xray.his.model.ConditionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Condition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/condition/convert")
@RequiredArgsConstructor
public class ConditionConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody ConditionData conditionData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        ConditionConvert conditionConvert = new ConditionConvert();
        Condition condition;
        String conditionSerialized;
        try {
            condition = conditionConvert.ConditionFHIR(conditionData);
            conditionSerialized = parser.encodeResourceToString(condition);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Condition error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(conditionSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addByEncounter(@RequestBody String input, @RequestParam(required = false) String encounter) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        ConditionConvert conditionConvert = new ConditionConvert();
        Condition condition;
        String conditionSerialized;
        Condition conditionData = parser.parseResource(Condition.class, input);
        try {
            condition = conditionConvert.addConditionEncounterFHIR(conditionData, encounter);
            conditionSerialized = parser.encodeResourceToString(condition);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Condition Add By Encounter error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(conditionSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/comorbidity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createChandoanKemTheo(@RequestBody Comorbidity comorbidity) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        ConditionConvert conditionConvert = new ConditionConvert();
        Condition condition;
        String conditionSerialized;
        try {
            condition = conditionConvert.ConditionCdkemtheoFHIR(comorbidity.getSubject(), comorbidity.getEncounter()
                    , comorbidity.getMaicd(), comorbidity.getChandoan(), comorbidity.getNgayud());
            conditionSerialized = parser.encodeResourceToString(condition);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Condition Comorbidity diagnostic error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(conditionSerialized, HttpStatus.CREATED);
    }
}
