package inext.ris.order.xray.hl7.obx;

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
@Table(name = "xray_hl7_obx")
@RequiredArgsConstructor
public class OBX {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "VALUE_TYPE")
    private String value_type;
    @Column(name = "OBS_IDEN")
    private String obs_iden;
    @Column(name = "OBS_SUB_ID")
    private String obs_sub_id;
    @Column(name = "OBS_VALUE")
    private String obs_value;
    @Column(name = "UNITS")
    private String units;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OBX obx = (OBX) o;
        return id != null && Objects.equals(id, obx.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
