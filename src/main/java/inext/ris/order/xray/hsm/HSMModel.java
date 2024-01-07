package inext.ris.order.xray.hsm;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_hsm")
@RequiredArgsConstructor
public class HSMModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "HOST")
    private String host;
    @Column(name = "PORT")
    private String port;
    @Column(name = "URL")
    private String url;
    @Column(name = "AUTH")
    private String auth;
    @Column(name = "XML")
    private String xml;
    @Column(name = "JSON")
    private String json;
    @Column(name = "PDF")
    private String pdf;
    @Column(name = "VALIDATION")
    private String validation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HSMModel hsmModel = (HSMModel) o;
        return getId() != null && Objects.equals(getId(), hsmModel.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
