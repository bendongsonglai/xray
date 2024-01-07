package inext.ris.order.xray.code;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_code")
@NoArgsConstructor
@AllArgsConstructor
public class CodeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CENTER", length = 10)
    private String center;
    @Column(name = "XRAY_CODE", length = 20)
    private String xray_code;
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    @Column(name = "XRAY_TYPE_CODE", length = 50)
    private String xray_type_code;
    @Column(name = "BODY_PART", length = 15)
    private String body_part;
    @Column(name = "CHARGE_TOTAL")
    private Float charge_total;
    @Column(name = "PORTABLE_CHARGE", length = 4)
    private int portable_charge;
    @Column(name = "DF", length = 5)
    private int df;
    @Column(name = "TIME_USE", length = 3)
    private int time_use;
    @Column(name = "BIRAD_FLAG", length = 1)
    private String birad_flag;
    @Column(name = "PREP_ID", length = 10)
    private String prep_id;
    @Column(name = "DELETE_FLAG", length = 1)
    private int delete_flag;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CodeModel codeModel = (CodeModel) o;
        return getId() != null && Objects.equals(getId(), codeModel.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
