package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.ServiceRequestConvert;
import inext.ris.order.xray.his.model.serviceRequest.ServiceRequestData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/servicerequest/convert")
@RequiredArgsConstructor
public class ServiceRequestConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody ServiceRequestData serviceRequestData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        ServiceRequestConvert serviceRequestConvert = new ServiceRequestConvert();
        ServiceRequest serviceRequest;
        String serviceRequestSerialized;
        try {
            serviceRequest = serviceRequestConvert.ServiceRequest(serviceRequestData);
            serviceRequestSerialized = parser.encodeResourceToString(serviceRequest);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert ServiceRequest error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(serviceRequestSerialized, HttpStatus.CREATED);
    }
}
