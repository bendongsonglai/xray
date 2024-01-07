package inext.ris.order.xray.ophthalmology;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Common {
    private String company;
    private String modelName;
    private String machineNo;
    private String rOMVersion;
    private String version;
    private String date;
    private String time;
    private Patient patient;
}
