package inext.ris.order.xray.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReportPublic {
    private String patientID;
    private String patientName;
    private String gender;
    private String dob;
    private String address;
    private String modality;
    private String accession;
    private String department;
    private String refererrer;
    private String performerCode;
    private String performerCchn;
    private String performerName;
    private String technical;
    private String workstation;
    private String coverage_type;
    private String note;
    private String reason;
    private String procedure;
    private String datetime;
    private String describe;
    private String conclusion;
    private String suggestion;
    private String dataQR;
}
