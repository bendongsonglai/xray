package inext.ris.order.xray.hapi.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CoverageData {
    private String subject;
    private String maql;
    private String madoituong;
    private String doituong;
}
