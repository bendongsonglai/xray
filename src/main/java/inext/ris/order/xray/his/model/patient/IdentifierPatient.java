package inext.ris.order.xray.his.model.patient;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IdentifierPatient {
    private String PI;
    private String NI;
    private String PPN;
    private String SB;
}
