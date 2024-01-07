package inext.ris.order.xray.his.model.patient;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AddressPatient {
    private String text;
    private List<String> line;
    private String city;
    private String district;
    private String country;
}
