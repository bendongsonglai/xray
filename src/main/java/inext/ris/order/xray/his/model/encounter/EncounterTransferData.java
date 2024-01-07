package inext.ris.order.xray.his.model.encounter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EncounterTransferData {
    private String encounter;
    private String transferDatetime;
}
