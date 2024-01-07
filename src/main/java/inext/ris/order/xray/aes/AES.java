package inext.ris.order.xray.aes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "xray_aes")
@RequiredArgsConstructor
public class AES {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SECRET")
    private String secret;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "content")
    private String content;
}
