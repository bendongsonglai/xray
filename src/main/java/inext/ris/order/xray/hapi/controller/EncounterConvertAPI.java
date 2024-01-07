package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.EncounterConvert;
import inext.ris.order.xray.his.model.encounter.EncounterData;
import inext.ris.order.xray.his.model.encounter.EncounterDischargeData;
import inext.ris.order.xray.his.model.encounter.EncounterTransferData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Encounter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/encounter/convert")
@RequiredArgsConstructor
public class EncounterConvertAPI {
    @RequestMapping(value = "admit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getListEncounter(@RequestParam String admit_number) {
        List<String> uuidList = new ArrayList<>();
        EncounterConvert encounterConvert = new EncounterConvert();
        try {
            uuidList = encounterConvert.findEncounterByIdentifier(admit_number);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Find Encounter By Admit_number error!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(uuidList);
    }

    @RequestMapping(value = "subject", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getListEncounterBySubject(@RequestParam String uuid) {
        List<String> uuidList = new ArrayList<>();
        EncounterConvert encounterConvert = new EncounterConvert();
        try {
            uuidList = encounterConvert.findEncounterBySubject(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Find Encounter By subject error!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(uuidList);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody EncounterData encounterData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        EncounterConvert encounterConvert = new EncounterConvert();
        Encounter encounter;
        String encounterSerialized;
        try {
            encounter = encounterConvert.EncounterAdmitFHIR(encounterData);
            encounterSerialized = parser.encodeResourceToString(encounter);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Encounter Admit error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(encounterSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "discharge", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEncounterDischarge(@RequestBody EncounterDischargeData encounterDischargeData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        EncounterConvert encounterConvert = new EncounterConvert();
        Encounter encounter;
        String encounterSerialized;
        try {
            encounter = encounterConvert.EncounterDischargeFHIR(encounterDischargeData);
            encounterSerialized = parser.encodeResourceToString(encounter);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Encounter Discharge error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(encounterSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "transfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEncounterTransfer(@RequestBody EncounterTransferData encounterTransferData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        EncounterConvert encounterConvert = new EncounterConvert();
        Encounter encounter;
        String encounterSerialized;
        try {
            encounter = encounterConvert.EncounterTransferFHIR(encounterTransferData);
            encounterSerialized = parser.encodeResourceToString(encounter);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Encounter Transfer error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(encounterSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "partof", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEncounterTransferPartOf(@RequestBody EncounterData encounterData,
                                                                @RequestParam(required = false) String encounterUUID) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        EncounterConvert encounterConvert = new EncounterConvert();
        Encounter encounter;
        String encounterSerialized;
        try {
            encounter = encounterConvert.EncounterTransferPartOfFHIR(encounterData, encounterUUID);
            encounterSerialized = parser.encodeResourceToString(encounter);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Encounter Transfer PartOf error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(encounterSerialized, HttpStatus.CREATED);
    }
}
