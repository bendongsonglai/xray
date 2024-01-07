package inext.ris.order.xray.hapi.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import inext.ris.order.xray.hapi.convert.OrganizationConvert;
import inext.ris.order.xray.his.model.OrganizationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fhir/api/v1/organization/convert")
@RequiredArgsConstructor
public class OrganizationConvertAPI {
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody OrganizationData organizationData) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        OrganizationConvert organizationConvert = new OrganizationConvert();
        Organization organization;
        String organizationSerialized;
        try {
            organization = organizationConvert.organizationHospitalFHIR(organizationData);
            organizationSerialized = parser.encodeResourceToString(organization);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Convert Organization Lab error!");
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(organizationSerialized, HttpStatus.CREATED);
    }
}
