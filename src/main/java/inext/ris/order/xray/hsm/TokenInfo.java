package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class TokenInfo {
    private String pin;
    private String serial;
}
