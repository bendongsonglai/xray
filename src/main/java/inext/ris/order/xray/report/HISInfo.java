package inext.ris.order.xray.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class HISInfo {
    private String url;
    private String user;
    private String password;
}
