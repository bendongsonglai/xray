package inext.ris.order.xray.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RecordsRemove {
    private Long id;
    private String mrn;
    private String accession;
    private Integer removed;
}
