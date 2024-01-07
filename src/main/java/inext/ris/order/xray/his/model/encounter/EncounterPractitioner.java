package inext.ris.order.xray.his.model.encounter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EncounterPractitioner {
    private String code;
    private String text;
    private String practitioner;
}
