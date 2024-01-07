package inext.ris.order.xray.convertDicom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InstanceModel {
    private String type;
    private String manufacturer;
    private String institutionName;
    private String referringPhysicianName;
    private String stationName;
    private String manufacturerModelName;
    private String mrn;
    private String accession;
    private String patientName;
    private String dob;
    private String gender;
    private String modality;
    private String studyDescription;
    private String studyDate;
    private String studyUID;
    private String seriesUID;
    private String instanceUID;
    private Integer instanceNumber;
    private Integer totalInstances;
    private String sOPInstanceUID;
    private String sOPClassesinStudy;
    private String content;
}
