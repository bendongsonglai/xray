package inext.ris.order.xray.hapi.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.hapi.convert.FHIRtype;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServiceRequestService implements ServiceRequestRepository{
    @Override
    public Boolean checkServiceRequsetExist(String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        try {
            Bundle bundle = client.search()
                    .forResource(ServiceRequest.class)
                    .where(ServiceRequest.IDENTIFIER.exactly().identifier(identifier))
                    .returnBundle(Bundle.class)
                    .execute();

            if (bundle.getTotal() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Check ServiceRequest with identifier {} error!", identifier);
        }
        log.info("Check ServiceRequest not found identifier {}", identifier);
        return false;
    }

    @Override
    public ServiceRequest getServiceRequsetIden(String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        ServiceRequest service = null;
        try {
           Bundle bundle = client.search()
                    .forResource(ServiceRequest.class)
                    .where(ServiceRequest.IDENTIFIER.exactly().identifier(identifier))
                    .returnBundle(Bundle.class)
                    .execute();
            if (bundle.getTotal() < 1) {
                log.info("Bundle ServiceRequest not found!");
                return null;
            }
            service = (ServiceRequest) bundle.getEntry().get(0).getResource();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get Service Request by identifier {}", identifier);
        }
        return service;
    }

    @Override
    public List<ServiceRequest> getListServiceRequestCode(String id, String code) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        List<ServiceRequest> listServiceRequest = new ArrayList<>();
        try {
            Bundle bundle = client
                    .search()
                    .forResource(ServiceRequest.class)
                    .where(ServiceRequest.IDENTIFIER.exactly().code(id))
                    .where(ServiceRequest.CODE.exactly().code(code))
                    .returnBundle(Bundle.class)
                    .execute();

            ServiceRequest serviceRequest = new ServiceRequest();
            if (bundle.getTotal() < 1) {
                log.info("Get List ServiceRequest by Code {} id {} size = 0!", code, id);
                return listServiceRequest;
            } else {
                for (int i = 0; i < bundle.getEntry().size(); i++) {
                    serviceRequest = (ServiceRequest) bundle.getEntry().get(i).getResource();
                    listServiceRequest.add(serviceRequest);
                }

                while (bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                    bundle = client
                            .loadPage()
                            .next(bundle)
                            .execute();
                    for (int i = 0; i < bundle.getEntry().size(); i++) {
                        serviceRequest = (ServiceRequest) bundle.getEntry().get(i).getResource();
                        listServiceRequest.add(serviceRequest);
                    }

                }
                log.info("Get List ServiceRequest by Code {} id {} Completed!", code, id);
                return listServiceRequest;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get List ServiceRequest by Code {} id {} error!", code, id);
        }
        return listServiceRequest;
    }
}
