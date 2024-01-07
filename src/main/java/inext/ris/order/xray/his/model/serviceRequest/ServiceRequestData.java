package inext.ris.order.xray.his.model.serviceRequest;

import inext.ris.order.xray.his.model.ConceptCode;
import inext.ris.order.xray.his.model.ConceptDiagnosis;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ServiceRequestData {
    private List<IdentifierServiceRequest> identifierServiceRequests;
    private List<ConceptCode> category;
    private IdentifierServiceRequest requisition;
    private ConceptCode code;
    private String subject;
    private String encounter;
    private String authoredOn;
    private String occurrenceDateTime;
    private String performer;
    private String requester;
    private ConceptDiagnosis reason;
    private String quantity;
}
