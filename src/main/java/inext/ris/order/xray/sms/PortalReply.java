package inext.ris.order.xray.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
public class PortalReply {
    private Long id;
    private String message_uid;
    private String by;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;
    private String description;

}
