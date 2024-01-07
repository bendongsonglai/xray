package inext.ris.order.xray.pacs;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_pacs")
@RequiredArgsConstructor
public class PacsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "HOST")
    private String host;
    @Column(name = "PORT")
    private String port;
    @Column(name = "AETITLE")
    private String aetitle;
    @Column(name = "HL7PORT")
    private String hl7port;
    @Column(name = "WADOPORT")
    private String wadoport;
    @Column(name = "WADOCONTEXT")
    private String wadocontext;
    @Column(name = "LOCAL_ARCHIVE_RIS")
    private String local_archive_ris;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PacsModel pacsModel = (PacsModel) o;
        return id != null && Objects.equals(id, pacsModel.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
