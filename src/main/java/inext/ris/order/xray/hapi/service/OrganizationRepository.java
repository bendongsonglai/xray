package inext.ris.order.xray.hapi.service;

import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Organization;

public interface OrganizationRepository {
    public Organization getOrganization(String makp);
    public Location getRoomFHIR(String makp);
    public Location getBedFHIR(String idphong);
}
