package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class SigningRequestContentPDF {
    private String data;
    private String documentName;
    private Location location;
    private ExtraInfo extraInfo;
    private String imageSignature;
}
