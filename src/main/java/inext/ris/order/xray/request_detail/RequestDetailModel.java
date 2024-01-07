package inext.ris.order.xray.request_detail;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_request_detail")
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "REQUEST_NO", length = 30)
    private String request_no;
    @Column(name = "ACCESSION", length = 30)
    private String accession;
    @Column(name = "XRAY_CODE", length = 30)
    private String xray_code;
    @Column(name = "WORKSTATION", length = 5)
    private String workstation;
    @Column(name = "DOITUONG", length = 5)
    private String doituong;
    @Column(name = "GHICHU")
    private String ghichu;
    @Column(name = "CHARGED")
    private String charged;
    @Column(name = "REQUEST_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @CreationTimestamp
    private Date request_timestamp;
    @Column(name = "REQUEST_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="HH:mm:ss", timezone = "UTC")
    private Date request_time;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date request_date;
    @Column(name = "SCHEDULE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date schedule_date;
    @Column(name = "SCHEDULE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="HH:mm:ss", timezone = "UTC")
    private Date schedule_time;
    @Column(name = "REPORT_TIME")
    @Temporal(TemporalType.TIME)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="HH:mm:ss", timezone = "UTC")
    private Date report_time;
    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date report_date;
    @Column(name = "REPORT_STATUS", length = 1)
    private String report_status;
    @Column(name = "CANCEL_STATUS", length = 1)
    private String cancel_status;
    @Column(name = "USER_ID_CANCEL", length = 10)
    private String user_id_cancel;
    @Column(name = "ARRIVAL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss")
    private Date arrival_time;
    @Column(name = "READY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date ready_time;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date start_time;
    @Column(name = "COMPLETE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date complete_time;
    @Column(name = "ASSIGN_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date assign_time;
    @Column(name = "APPROVED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date approved_time;
    @Column(name = "ASSIGN", length = 20)
    private String assign;
    @Column(name = "ASSIGN_BY", length = 20)
    private String assign_by;
    @Column(name = "STATUS", length = 10)
    private String status;
    @Column(name = "PAGE", length = 15)
    private String page;
    @Column(name = "LOCKBY", length = 15)
    private String lockby;
    @Column(name = "URGENT", length = 1)
    private String urgent;
    @Column(name = "LASTREPORT_ID", length = 20)
    private String lastreport_id;
    @Column(name = "TEMP_REPORT")
    private String temp_report;
    @Column(name = "AUTO_SAVE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date auto_save_time;
    @Column(name = "TECH1", length = 5)
    private String tech1;
    @Column(name = "TECH2", length = 5)
    private String tech2;
    @Column(name = "TECH3", length = 5)
    private String tech3;
    @Column(name = "FLAG1")
    private Integer flag1;
    @Column(name = "FLAG2")
    private Integer flag2;
    @Column(name = "FLAG3")
    private Integer flag3;
    @Column(name = "REPORT_BOOK")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date report_book;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        RequestDetailModel that = (RequestDetailModel) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
