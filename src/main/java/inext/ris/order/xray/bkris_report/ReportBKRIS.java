package inext.ris.order.xray.bkris_report;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "xray_report")
@RequiredArgsConstructor
public class ReportBKRIS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "DESCRIBEREPORT")
    private String describereport;
    @Column(name = "CONCLUSION")
    private String conclusion;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "BIRAD")
    private String birad;
    @Column(name = "HISTORY")
    private String history;
    @Column(name = "CALCIUM")
    private String calcium;
    @Column(name = "CORONARY")
    private String coronary;
    @Column(name = "KEY_IMAGE_LINK")
    private String key_image_link;
    @Column(name = "DICTATE_BY")
    private String dictate_by;
    @Column(name = "DICTATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dictate_date;
    @Column(name = "DICTATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="HH:mm:ss")
    private Date dictate_time;
    @Column(name = "APPROVE_BY")
    private String approve_by;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date approve_date;
    @Column(name = "APPROVE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="HH:mm:ss")
    private Date approve_time;
    @Column(name = "STATUS")
    private String status;
}
