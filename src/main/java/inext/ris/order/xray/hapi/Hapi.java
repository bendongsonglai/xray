package inext.ris.order.xray.hapi;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "xray_hapi_server")
@RequiredArgsConstructor
public class Hapi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "IP")
    private String ip;
    @Column(name = "PORT")
    private String port;
    @Column(name = "PATH")
    private String path;
    @Column(name = "USER")
    private String user;
    @Column(name = "PASS")
    private String pass;
}
