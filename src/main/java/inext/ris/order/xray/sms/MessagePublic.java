package inext.ris.order.xray.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
public class MessagePublic {
    private String message_id;
    private String mrn;
    private String title;
    private String physicans;
    private String publisher;
    private String purpose;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
}
