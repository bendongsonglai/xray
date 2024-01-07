package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ResponseContent {
    private String documentName;
    private String signatureOnly;
    private String signedDocument;
}
