package inext.ris.order.xray.his.model.patient;

import inext.ris.order.xray.his.model.ConceptCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContactPatient {
    private ConceptCode relationship;
    private String name;
    private ContactPointPatient telecom;
    private String address;
    private String gender;
}
