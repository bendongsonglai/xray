package inext.ris.order.xray.his.model.encounter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EncounterDiagnosis {
    private String type;
    private String icd;
    private String diagnosis;
}
