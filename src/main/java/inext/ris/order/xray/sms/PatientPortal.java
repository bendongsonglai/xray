package inext.ris.order.xray.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class PatientPortal {
    private String mrn;
    private String name;
    private String telephone;
    private String email;
    private String secret;
    private String otp;
    private String linkportal;
}
