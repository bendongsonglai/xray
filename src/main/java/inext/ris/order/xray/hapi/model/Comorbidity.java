package inext.ris.order.xray.hapi.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Comorbidity {
    private String subject;
    private String encounter;
    private String maicd;
    private String chandoan;
    private String ngayud;
}
