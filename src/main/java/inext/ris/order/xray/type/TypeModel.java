package inext.ris.order.xray.type;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "xray_type")
@RequiredArgsConstructor
public class TypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "XRAY_TYPE_CODE")
    private String xray_type_code;
    @Column(name = "TYPE_NAME")
    private String type_name;
    @Column(name = "TYPE_NAME_ENG")
    private String type_name_eng;
    @Column(name = "MOD_TYPE")
    private String mod_type; 
    @Column(name = "DETAIL")
    private String detail;
}
