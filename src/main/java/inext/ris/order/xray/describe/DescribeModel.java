package inext.ris.order.xray.describe;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "xray_describe")
@RequiredArgsConstructor
public class DescribeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID")
    private String user_id;
    @Column(name = "TERM")
    private String term;
    @Column(name = "VALUE")
    private String value;
    @Column(name = "STATUS")
    private Integer status;
}
