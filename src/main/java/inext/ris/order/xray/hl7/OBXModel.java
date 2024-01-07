package inext.ris.order.xray.hl7;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class OBXModel {
    private Integer index;
    private String type;
    private String obs_iden;
    private String obs_sub;
    private String obs_val;
    private String units;
}
