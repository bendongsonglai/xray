package inext.ris.order.xray.reportsigned;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "xray_signature")
@RequiredArgsConstructor
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "SIGNATURE")
    private String signature;
    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date created_time;
}
