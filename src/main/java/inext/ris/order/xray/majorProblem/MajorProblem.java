package inext.ris.order.xray.majorProblem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "xray_major_problem")
@RequiredArgsConstructor
public class MajorProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "VALUE")
    private String value;
    @Column(name = "ENABLED")
    private Integer enabled;
}
