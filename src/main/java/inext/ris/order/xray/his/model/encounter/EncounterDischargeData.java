package inext.ris.order.xray.his.model.encounter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EncounterDischargeData {
    private String subject;
    private String encounter;
    private String dischargeDatetime;
    private EncounterPractitioner encounterPractitioner;
    private EncounterDiagnosis encounterDiagnosis;
    private String result;
}
