package inext.ris.order.xray.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Report {
    private String uri;
    private String user;
    private String password;
    private String content;
    private String directorySync;
    private String secretkey;
    private String qrhome;
    private String linkQR;
}
