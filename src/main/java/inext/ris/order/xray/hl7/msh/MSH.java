package inext.ris.order.xray.hl7.msh;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_hl7_msh")
@RequiredArgsConstructor
public class MSH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SEND_APP")
    private String send_app;
    @Column(name = "SEND_FAC")
    private String send_fac;
    @Column(name = "RECE_APP")
    private String rece_app;
    @Column(name = "RECE_FAC")
    private String rece_fac;
    @Column(name = "COUNTRY_CODE")
    private String country_code;
    @Column(name = "CHARACTER")
    private String character;
    @Column(name = "LANGUAGE_MESSAGE")
    private String language_message;
    @Column(name = "DIRECTORY")
    private String directory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MSH mshmodel = (MSH) o;
        return id != null && Objects.equals(id, mshmodel.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
