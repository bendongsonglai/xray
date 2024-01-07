package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Authenticate {
    private String username;
    private String password;
    private Boolean rememberMe;
}
