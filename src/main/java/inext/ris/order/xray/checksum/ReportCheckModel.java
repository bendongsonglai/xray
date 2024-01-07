package inext.ris.order.xray.checksum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor

public class ReportCheckModel {
    private String filename;
    private String data;
}
