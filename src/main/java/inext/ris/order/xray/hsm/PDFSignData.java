package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class PDFSignData {
    private List<SigningRequestContent> signingRequestContents;
    private TokenInfo tokenInfo;
    private Optional optional;
}
