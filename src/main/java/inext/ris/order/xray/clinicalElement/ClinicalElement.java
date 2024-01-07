package inext.ris.order.xray.clinicalElement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Entity
@Setter
@Getter
@Table(name = "xray_clinical_element")
@RequiredArgsConstructor
public class ClinicalElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "DEFAULT_VAL")
    private String default_val;
    @Column(name = "UNIT")
    private String unit;
    @Column(name = "ENABLED")
    private Integer enabled;
}
