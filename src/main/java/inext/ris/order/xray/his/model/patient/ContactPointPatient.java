package inext.ris.order.xray.his.model.patient;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContactPointPatient {
    private String system;
    private String value;
    private String use;
}
