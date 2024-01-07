package inext.ris.order.xray.referrer;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_referrer")
@NoArgsConstructor
@AllArgsConstructor
public class ReferrerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "REFERRER_ID", length = 10)
    private String referrer_id;
    @Column(name = "DEGREE", length = 50)
    private String degree;
    @Column(name = "NAME", length = 50)
    private String name;
    @Column(name = "LASTNAME", length = 50)
    private String lastname;
    @Column(name = "NAME_ENG", length = 50)
    private String name_eng;
    @Column(name = "LASTNAME_ENG", length = 50)
    private String lastname_eng;
    @Column(name = "PREFIX", length = 3)
    private String prefix;
    @Column(name = "SEX", length = 5)
    private String sex;
    @Column(name = "CENTER_CODE", length = 10)
    private String center_code;
    @Column(name = "PRACTITIONER_ID", length = 50)
    private String practitioner_id;
    @PostPersist
    public void onSave(){
    	referrer_id = "" + id + "";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ReferrerModel that = (ReferrerModel) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
