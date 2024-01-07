package inext.ris.order.xray.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_exam")
@RequiredArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "DIRECTORY")
    private String directory;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "LAST_STATUS")
    private String last_status;
    @Column(name = "REMOVE")
    private Integer remove;
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date created;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date last_update;
    @Column(name = "VERSION")
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Exam exam = (Exam) o;
        return getId() != null && Objects.equals(getId(), exam.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
