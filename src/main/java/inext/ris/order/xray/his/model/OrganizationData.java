package inext.ris.order.xray.his.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrganizationData {
    private String provider_license_number;
    private String provider_code;
    private String name;
    private String telecomPhone;
    private String telecomTax;
    private String telecomMail;
    private String addressText;
    private String addressLine;
    private String addressDistrict;
    private String addressCity;
}
