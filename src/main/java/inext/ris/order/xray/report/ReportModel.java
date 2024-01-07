package inext.ris.order.xray.report;

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
@Table(name = "xray_report")
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACCESSION", length = 30)
    private String accession;
    @Column(name = "DESCRIBEREPORT")
    private String describereport;
    @Column(name = "CONCLUSION")
    private String conclusion;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "TECHNICAL")
    private String technical;
    @Column(name = "WORKSTATION", length = 10)
    private String workstation;
    @Column(name = "DOITUONG", length = 10)
    private String doituong;
    @Column(name = "BIRAD", length = 1)
    private String birad;
    @Column(name = "HISTORY", length = 5000)
    private String history;
    @Column(name = "CALCIUM", length = 5000)
    private String calcium;
    @Column(name = "CORONARY", length = 5000)
    private String coronary;
    @Column(name = "KEY_IMAGE_LINK", length = 5000)
    private String key_image_link;
    @Column(name = "DICTATE_BY", length = 50)
    private String dictate_by;
    @Column(name = "DICTATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date dictate_date;
    @Column(name = "DICTATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss", timezone = "UTC")
    private Date dictate_time;
    @Column(name = "APPROVE_BY", length = 50)
    private String approve_by;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date approve_date;
    @Column(name = "APPROVE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss", timezone = "UTC")
    private Date approve_time;
    @Column(name = "STATUS_REPORT", length = 20)
    private String status;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ReportModel that = (ReportModel) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
