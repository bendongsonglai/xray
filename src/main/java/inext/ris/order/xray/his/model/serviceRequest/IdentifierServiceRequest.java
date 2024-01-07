package inext.ris.order.xray.his.model.serviceRequest;

import inext.ris.order.xray.his.model.ConceptCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IdentifierServiceRequest {
    private String use;
    private ConceptCode type;
    private String system;
    private String value;
}
