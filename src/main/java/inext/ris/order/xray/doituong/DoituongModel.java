package inext.ris.order.xray.doituong;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "xray_doituong")
@RequiredArgsConstructor
public class DoituongModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TYPE_ID")
    private String type_id;
    @Column(name = "NAME")
    private String name;
}
