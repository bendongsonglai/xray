package inext.ris.order.xray.his.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConceptCode {
    private String system;
    private String code;
    private String value;
    private String text;
}
