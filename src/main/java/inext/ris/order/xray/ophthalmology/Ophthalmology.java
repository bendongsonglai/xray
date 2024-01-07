package inext.ris.order.xray.ophthalmology;

import inext.ris.order.xray.convertDicom.InstanceModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Ophthalmology {
    private InstanceModel instanceModel;
    private Common common;
    private Measure measure;
}
