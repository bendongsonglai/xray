package inext.ris.order.xray.exam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ToExam {
    private String source;
    private String destination;
    private String accession;
}
