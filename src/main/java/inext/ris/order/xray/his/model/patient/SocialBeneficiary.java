package inext.ris.order.xray.his.model.patient;

import inext.ris.order.xray.his.model.PeriodTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SocialBeneficiary {
    private PeriodTime periodTime;
    private String number;
}
