package inext.ris.order.xray.hapi.service;

import org.hl7.fhir.r4.model.ServiceRequest;

import java.util.List;

public interface ServiceRequestRepository {
    public Boolean checkServiceRequsetExist(String identifier);
    public ServiceRequest getServiceRequsetIden(String identifier);
    public List<ServiceRequest> getListServiceRequestCode(String id, String code);
}
