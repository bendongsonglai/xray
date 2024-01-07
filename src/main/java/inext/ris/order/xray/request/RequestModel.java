package inext.ris.order.xray.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_request")
@NoArgsConstructor
@AllArgsConstructor
public class RequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "REQUEST_NO", length = 30)
    private String request_no;
    @Column(name = "MRN", length = 50)
    private String mrn;
    @Column(name = "XN", length = 10)
    private String xn;
    @Column(name = "REFERRER", length = 20)
    private String referrer;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date request_date;
    @Column(name = "REQUEST_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date request_timestamp;
    @Column(name = "DEPARTMENT_ID", length = 10)
    private String department_id;
    @Column(name = "PORTABLE")
    private Integer portable;
    @Column(name = "USER", length = 10)
    private String user;
    @Column(name = "CANCEL_STATUS")
    private Integer cancel_status;
    @Column(name = "USER_ID_CANCLE", length = 10)
    private String user_id_cancle;
    @Column(name = "CANCEL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date cancel_date;
    @Column(name = "CANCEL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="HH:mm:ss")
    private Date cancel_time;
    @Column(name = "STATUS", length = 10)
    private String status;
    @Column(name = "ICON", length = 50)
    private String icon;
    @Column(name = "NOTE", length = 500)
    private String note;
    @Column(name = "CENTER_CODE", length = 10)
    private String center_code;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        RequestModel that = (RequestModel) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
