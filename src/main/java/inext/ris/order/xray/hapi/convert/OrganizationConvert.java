package inext.ris.order.xray.hapi.convert;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.his.model.OrganizationData;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class OrganizationConvert {
    private static IGenericClient client;
    public Organization organizationHospitalFHIR(OrganizationData organizationData) {
        Organization organization = new Organization();
        FHIRtype FHIRtype = new FHIRtype();
        client = FHIRtype.connectFHIR();
        String id = UUID.randomUUID().toString();
        organization.setId(IdType.URN_PREFIX + "uuid:"+id);
        organization.setIdBase(id);
        organization.setMeta(new Meta().addProfile("http://moh.gov.vn/fhir/core/StructureDefinition/vn-core-provider"));

        organization.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "LN" , "License number", "http://moh.gov.vn/fhir/core/sid/provider-license-number", organizationData.getProvider_license_number()));
        organization.addIdentifier(FHIRtype.identifier("http://terminology.hl7.org/CodeSystem/v2-0203", "PRN" , "Provider number", "http://moh.gov.vn/fhir/core/sid/provider-code", organizationData.getProvider_code()));

        organization.setActive(true);
        organization.addType(FHIRtype.code("http://terminology.hl7.org/CodeSystem/organization-type", "prov", "Healthcare Provider"));
        organization.setName(organizationData.getName());

        List<ContactPoint> listTelecom = new ArrayList<ContactPoint>();
        ContactPoint telecomPhone = new ContactPoint();
        telecomPhone.setSystem(ContactPoint.ContactPointSystem.PHONE);
        telecomPhone.setValue(organizationData.getTelecomPhone());
        ContactPoint telecomFax = new ContactPoint();
        telecomFax.setSystem(ContactPoint.ContactPointSystem.FAX);
        telecomFax.setValue(organizationData.getTelecomTax());
        ContactPoint telecomMail = new ContactPoint();
        telecomMail.setSystem(ContactPoint.ContactPointSystem.EMAIL);
        telecomMail.setValue(organizationData.getTelecomMail());
        listTelecom.add(telecomPhone);
        listTelecom.add(telecomFax);
        listTelecom.add(telecomMail);
        organization.setTelecom(listTelecom);

        Address address = new Address();
        address.setText(organizationData.getAddressText());
        address.addLine(organizationData.getAddressLine());
        address.setDistrict(organizationData.getAddressDistrict());
        address.setCity(organizationData.getAddressCity());
        organization.addAddress(address);
        String responseID = client.update().resource(organization).execute().getId().getValue();
        log.info("Creating Organization Lab uuid {}",responseID);
        return getOrganizationByUUID(responseID);
    }

    private static Organization getOrganizationByUUID (String uuid) {
        Organization organization = new Organization();
        try {
            FHIRtype FHIRtype = new FHIRtype();
            client = FHIRtype.connectFHIR();
            organization = client.read().resource(Organization.class).withId(uuid).execute();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fetch Organization By uuid {} error!", uuid);
        }
        return organization;
    }
}
