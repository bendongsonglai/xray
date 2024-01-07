package inext.ris.order.xray.fhir1;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "xray_fhir1")
@RequiredArgsConstructor
public class FHIRModel1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "BUNDLE_ID")
    private String bundle_id;
    @Column(name = "SERVICE_ID")
    private String service_id;
    @Column(name = "ACCESSION_NUMBER")
    private String accession_number;
    @Column(name = "IUID")
    private String iuid;
    @Column(name = "ENCOUNTER_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date encounter_time;
    @Column(name = "SERVICE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date service_time;
    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created_time;
    @Column(name = "STATUS")
    private String status;
}
