package inext.ris.order.xray.his.model.patient;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r4.model.Patient;

@Data
@RequiredArgsConstructor
public class BundlePatient {
    private Boolean check;
    private Patient patient;
}
