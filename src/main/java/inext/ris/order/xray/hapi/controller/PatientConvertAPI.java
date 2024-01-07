package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.PatientConvert;
import inext.ris.order.xray.his.model.patient.PatientData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/patient/convert")
@RequiredArgsConstructor
public class PatientConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody PatientData patientData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        PatientConvert patientConvert = new PatientConvert();
        Patient patient;
        String patientSerialized;
        try {
            patient = patientConvert.PatientFHIR(patientData);
            patientSerialized = parser.encodeResourceToString(patient);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Patient error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(patientSerialized, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@RequestBody PatientData patientData, @PathVariable String uuid) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        PatientConvert patientConvert = new PatientConvert();
        Patient patient;
        String patientSerialized;
        try {
            patient = patientConvert.PatientFHIRUpdate(patientData, uuid);
            patientSerialized = parser.encodeResourceToString(patient);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Update Convert Patient Update error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(patientSerialized, HttpStatus.CREATED);
    }
}
