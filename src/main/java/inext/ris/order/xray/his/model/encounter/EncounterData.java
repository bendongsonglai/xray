package inext.ris.order.xray.his.model.encounter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class EncounterData {
    private String medicalRecordNumber;
    private String admitNumber;
    private String managementNumber;
    private String subject;
    private String classOrigin;
    private String admitDatetime;
    private String locationWardCode;
    private String locationBedCode;
    private String datetime;
    private List<EncounterDiagnosis> encounterDiagnosisList;
    private String locationTransferCode;
    private List<EncounterPractitioner> encounterPractitionerList;
    private String organizationCode;
}
