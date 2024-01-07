package inext.ris.order.xray.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class iDiVi {
    private String user;
    private String accession;
    private String dateStart;
    private String describe;
    private String conclusion;
    private String suggestion;
}
