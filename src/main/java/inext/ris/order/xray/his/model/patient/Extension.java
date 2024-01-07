package inext.ris.order.xray.his.model.patient;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Extension {
    private String system;
    private String code;
    private String text;
}
