package inext.ris.order.xray.patient_diagnosis;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
public class PatientData {
    private String mrn;
    private String acc;
    private String ce_name;
    private String ce_val;
    private String unit;
    private String mp_name;
    private String mp_val;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date date;
}
