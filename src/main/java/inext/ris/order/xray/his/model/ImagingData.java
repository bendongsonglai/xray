package inext.ris.order.xray.his.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ImagingData {
    private String accessionNumber;
    private String modality;
    private String description;
    private String actor;

    private String subject;
}
