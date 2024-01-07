package inext.ris.order.xray.patient_info;

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
@Table(name = "xray_patient_info")
@NoArgsConstructor
@AllArgsConstructor
public class PatientInfoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CENTER_CODE", length = 10)
    private String center_code;
    @Column(name = "MRN", length = 20)
    private String mrn;
    @Column(name = "XN", length = 20)
    private String xn;
    @Column(name = "SSN", length = 255)
    private String ssn;
    @Column(name = "PREFIX", length = 50)
    private String prefix;
    @Column(name = "NAME", length = 120)
    private String name;
    @Column(name = "LASTNAME", length = 120)
    private String lastName;
    @Column(name = "NAME_ENG", length = 120)
    private String name_Eng;
    @Column(name = "LASTNAME_ENG", length = 120)
    private String lastName_Eng;
    @Column(name = "NAME_OLD", length = 50)
    private String name_Old;
    @Column(name = "LASTNAME_OLD", length = 50)
    private String lastName_Old;
    @Column(name = "SEX", length = 10)
    private String sex;
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date birth_Date;
    @Column(name = "TELEPHONE", length = 15)
    private String telephone;
    @Column(name = "EMAIL", length = 50)
    private String email;
    @Column(name = "NOTE", length = 2000)
    private String note;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Date create_Date;
    @Column(name = "FIRSTVISITDATE")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date firstVisitDate;
    @Column(name = "LASTVISITDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private Date lastVisitDate;
    @Column(name = "RIGHT_CODE", length = 10)
    private String right_Code;
    @Column(name = "ADDRESS", length = 500)
    private String address;
    @Column(name = "VILLAGE", length = 50)
    private String village;
    @Column(name = "ROAD", length = 50)
    private String road;
    @Column(name = "TAMBON_CODE", length = 50)
    private String tambon_Code;
    @Column(name = "AMPHOE_CODE", length = 50)
    private String amphoe_Code;
    @Column(name = "PROVINCE_CODE", length = 20)
    private String province_Code;
    @Column(name = "COUNTRY_CODE", length = 50)
    private String country_Code;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PatientInfoModel that = (PatientInfoModel) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
