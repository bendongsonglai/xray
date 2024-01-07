package inext.ris.order.xray.his.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PractitionerData {
    private String certification;
    private String name;
    private String phone;
    private String address;
    private String mabs;
    private String gender;
    private String organization;
}
