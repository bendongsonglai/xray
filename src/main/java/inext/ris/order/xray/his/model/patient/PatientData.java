package inext.ris.order.xray.his.model.patient;

import inext.ris.order.xray.his.model.PeriodTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PatientData {
    private String patientID;
    private String nationalID;
    private String passPort;
    private String socialBeneficiaryNumber;
    private List<ContactPointPatient> telecom;
    private PeriodTime socialBeneficiaryPeriod;
    private List<Extension> extensionList;
    private String patientName;
    private AddressPatient address;
    private String gender;
    private String birthDate;
    private ContactPatient contact;
    private String OrganizationUUID;
}
