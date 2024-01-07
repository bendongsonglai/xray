package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.his.model.LocationData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.util.List;
import java.util.UUID;
@Slf4j
public class LocationConvert {
    private static IGenericClient client;
    public Location locationHospitalFHIR(LocationData locationData) {
        Location location = new Location();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        String id = UUID.randomUUID().toString();
        location.setId(IdType.URN_PREFIX + "uuid:"+id);
        location.setIdBase(id);
        location.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-location"));
        location.addIdentifier(FHIRtype.identifier("", "" , "", "http://hospital.example.vn/DEPARTMENT_ID", locationData.getMakp()));
        location.setStatus(Location.LocationStatus.ACTIVE);
        location.setName(locationData.getTenkp());
        location.setDescription(locationData.getTenkp());
        location.setManagingOrganization(new Reference("Organization/" + getUUIDLocationByCode(locationData.getOrganizationCode())));
        String responseID = client.update().resource(location).execute().getId().getValue();
        log.info("Creating Location uuid {}",responseID);
        return getLocationByUUID(responseID);
    }

    private static Location getLocationByUUID (String uuid) {
        Location location = new Location();
        try {
            FHIRtype FHIRtype = new FHIRtype();
            client = FHIRtype.connectFHIR();
            location = client.read().resource(Location.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Organization By uuid {} error!", uuid);
        }
        return location;
    }

    private static String getUUIDLocationByCode (String code) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Organization.class)
                .where(Organization.IDENTIFIER.exactly().identifier(code)).returnBundle(Bundle.class).execute();
        log.info("Get Organization code {}", code);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
        }
        return id;
    }

    public Boolean checkLocationByCode (String code) {
        IGenericClient client1;
        FHIRtype FHIRtype = new FHIRtype();
        client1 = FHIRtype.connectFHIR();
        Bundle results = client1.search().forResource(Location.class)
                .where(Location.IDENTIFIER.exactly().identifier(code)).returnBundle(Bundle.class).execute();
        //log.info("check Location MAKP {}", code);
        String id = null;
        if (results.getTotal() != 0) {
            List<Bundle.BundleEntryComponent> components = results.getEntry();
            for (Bundle.BundleEntryComponent component : components) {
                id = component.getResource().getIdElement().getIdPart();
            }
            if (id.length() > 0) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        //checkLocationByCode("Cấp Cứu") ;
    }

}
