package inext.ris.order.xray.report.title;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_report_title")
@RequiredArgsConstructor
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "XRAY_TYPE_CODE")
    private String xray_type_code;
    @Column(name = "XRAY_CODE")
    private String xray_code;
    @Column(name = "DES_VN")
    private String des_vn;
    @Column(name = "DES_EN")
    private String des_en;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Title title = (Title) o;
        return id != null && Objects.equals(id, title.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
