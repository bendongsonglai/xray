package inext.ris.order.xray.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class Account {
    private Long id;
    private String mrn;
    private String telephone;
    private String email;
    private String secret;
    private String otp;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created_time;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastupdated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date otp_time;
    private Integer enable;
}
