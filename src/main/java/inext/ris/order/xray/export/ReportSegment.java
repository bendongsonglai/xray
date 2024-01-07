package inext.ris.order.xray.export;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ReportSegment {
    private String dateTime;
    private String title;
    private String provider;
    private String xray_code;
    private String describe;
    private String conclusion;
    private String note;
    private String sign;
    private String radiogist;
}
