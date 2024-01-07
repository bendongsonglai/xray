package inext.ris.order.xray.hl7;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/hl7")
@Slf4j
@RequiredArgsConstructor
public class HL7API {
	@Autowired
	HL7Service hl7Service;
	
    @PostMapping
    public void create(@Valid @RequestBody HL7Model hl7) {
    	if (hl7.getImagingIdentifier().isEmpty()) {
    		log.error("AccessionNumber Empty");
    	}
    	hl7Service.createFileHL7(hl7);
    }
}
