package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class PDFSignPositionData {
    private List<SigningRequestContentPDF> signingRequestContents;
    private TokenInfo tokenInfo;
    private Optional optional;
}
