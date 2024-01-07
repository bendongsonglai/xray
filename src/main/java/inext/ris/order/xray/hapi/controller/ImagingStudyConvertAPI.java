package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.ImagingDiagnosticConvert;
import inext.ris.order.xray.his.model.ImagingData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/imaging/convert")
@RequiredArgsConstructor
public class ImagingStudyConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody ImagingData imagingData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        ImagingDiagnosticConvert imagingDataConvert = new ImagingDiagnosticConvert();
        ImagingStudy imagingStudy;
        String imagingStudySerialized;
        try {
            imagingStudy = imagingDataConvert.ImagingConvertor(imagingData);
            imagingStudySerialized = parser.encodeResourceToString(imagingStudy);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert imagingStudy error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(imagingStudySerialized, HttpStatus.CREATED);
    }
}
