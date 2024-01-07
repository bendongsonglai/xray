package inext.ris.order.xray.department;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.text.DecimalFormat;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_department")
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CENTER", length = 10)
    private String center;
    @Column(name = "DEPARTMENT_ID", length = 10)
    private String department_id;
    @Column(name = "NAME_VIE", length = 50)
    private String name_vie;
    @Column(name = "NAME_ENG", length = 50)
    private String name_eng;
    @Column(name = "TYPE", length = 5)
    private String type;
    @PostPersist
    public void onSave(){
    	DecimalFormat df = new DecimalFormat("000");
    	department_id = "" + df.format(id) + "";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DepartmentModel that = (DepartmentModel) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
