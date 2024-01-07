package inext.ris.order.xray.hl7;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class HL7ReportModel {
    private String directory;
    private String send_app;
    private String send_fac;
    private String rece_app;
    private String rece_fac;
    private String dateTimeOfMessage;
    private String accessionNumber;
    private String authoredOn;
    private String pid;
    private String family;
    private String given;
    private String name;
    private String birthdate;
    private String gender;
    private String race;
    private String address;
    private String country;
    private String phoneHome;
    private String phoneBusiness;
    private String ethic;
    private String location;
    private String referrer_code;
    private String referrer_name;
    private String referrer_lastname;
    private String provider;
    private String consult_name;
    private String consult_lastname;
    private String consult_position;
    private String dateTimeofTransaction;
    private String doc_code;
    private String doc_type;
    private String activityDateTime;
    private String originationDateTime;
    private String editDateTime;
    private String ce_mp;
    private String xray_code;
    private String procedure;
    private String recordedDateTime;
    private String reason;
    private String resultsDateTime;
    private String modality;
    private List<OBXModel> obxList;
}
