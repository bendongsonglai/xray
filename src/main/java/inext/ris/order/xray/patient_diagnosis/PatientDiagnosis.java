package inext.ris.order.xray.patient_diagnosis;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "xray_patient_diagnosis")
@RequiredArgsConstructor
public class PatientDiagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "MRN")
    private String mrn;
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "CE")
    private Integer ce;
    @Column(name = "CE_VAL")
    private String ce_val;
    @Column(name = "MP")
    private Integer mp;
    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date date;
    @Column(name ="ENABLED")
    private Integer enabled;
}
