package inext.ris.order.xray.localService;

import inext.ris.order.xray.config.PortalProperties;
import inext.ris.order.xray.sms.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
@Service
@Slf4j
public class WebServicePortal {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(6);
    private final WebClient portalApiClient;

    //@Autowired
    /* Chú ý chỗ này cấu hình thông quan file config ngoài */
    public WebServicePortal() {
        PortalProperties portalProperties= new PortalProperties();
        portalProperties.LoadProperties();
        String portalUrl = portalProperties.getPortalURL();
        this.portalApiClient = WebClient.builder ()
                .baseUrl("http://"+portalUrl+":9092/api/v1").build();;
    }
    public Account getPortalInfo(String mrn) {
        Mono<Account> accountMono = null;
        try {
            accountMono = portalApiClient.get()
                    .uri("/account/get/info?mrn=" + mrn)
                    .retrieve()
                    .bodyToMono(Account.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountMono.block();
    }

    public String ResetPassword(String mrn) {
      String accountMono = null;
        try {
            accountMono = String.valueOf(portalApiClient.post()
                    .uri("/account/reset/"+mrn)
                    .bodyValue(new String(""))
                    .exchange()
                    .flatMap(res -> res.bodyToMono(String.class))
                    .subscribe(password -> System.out.println("POST reset new password: " + mrn+ ", " + password)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountMono;
    }

    public ResponseMessage createMessage(MessagePublic message) {
        Mono<PortalMessage> messageMono = null;
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            messageMono = portalApiClient.post()
                    .uri("/portal_message/message")
                    .bodyValue(message)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(PortalMessage.class));
            responseMessage.setMessage_id(messageMono.block().getMessage_uid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseMessage;
    }

    public PortalReply replyMessage(PortalReply portalReply) {
        Mono<PortalReply> messageMono = null;
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            messageMono = portalApiClient.post()
                    .uri("/portal_message/message_reply")
                    .bodyValue(portalReply)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(PortalReply.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageMono.block();
    }

    public PortalMessage getPortalMessage(String message_uid) {
        Mono<PortalMessage> portalMessageMono = null;
        try {
            portalMessageMono = portalApiClient.get()
                    .uri("/portal_message/public?uid=" + message_uid)
                    .retrieve()
                    .bodyToMono(PortalMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portalMessageMono.block();
    }

    public PortalReply getPortalReply(String message_uid) {
        Mono<PortalReply> portalReplyMono = null;
        try {
            portalReplyMono = portalApiClient.get()
                    .uri("/portal_reply/public?uid=" + message_uid)
                    .retrieve()
                    .bodyToMono(PortalReply.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portalReplyMono.block();
    }

    public Message getMessage(String message_uid) {
        Mono<Message> portalMessageMono = null;
        try {
            portalMessageMono = portalApiClient.get()
                    .uri("/portal_message/details?uid=" + message_uid)
                    .retrieve()
                    .bodyToMono(Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portalMessageMono.block();
    }

    public void removeRecord(RecordsRemove remove) {
        try {
           portalApiClient.post()
                    .uri("/remove/record")
                    .bodyValue(remove)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(RecordsRemove.class))
                    .subscribe(password -> System.out.println("POST remove accession : " + password.getAccession()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean checkAccession(String acc) {
        Boolean check = false;
        try {
            check = portalApiClient
                    .get()
                    .uri("/remove/check/" + acc)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(REQUEST_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }
}
