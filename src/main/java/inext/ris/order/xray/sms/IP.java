package inext.ris.order.xray.sms;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_portal_info")
@RequiredArgsConstructor
public class IP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "IP")
    private String ip;
    @Column(name = "LINK")
    private String link;
    @Column(name = "ENABLED")
    private Integer enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IP ip = (IP) o;
        return id != null && Objects.equals(id, ip.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
