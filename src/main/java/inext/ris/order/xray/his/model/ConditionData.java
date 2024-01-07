package inext.ris.order.xray.his.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConditionData {
    private String subject;
    private String encounter;
    private String icd;
    private String diagnosis;
    private String admitDatetime;
    private String practitioner;
}
