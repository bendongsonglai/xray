package inext.ris.order.xray.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
public class PortalMessage {
    private String mrn;
    private String message_uid;
    private String publisher;
    private String physicans;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    private Integer status;;
}
