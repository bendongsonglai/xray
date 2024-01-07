package inext.ris.order.xray.hl7;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HL7Model {
	private String destination;
	private String organization;
	private String pacs;
	private String bundleID;
	private String patientID;
	private String patientName;
	private String patientBirthDate;
	private String patientGender;
	private String patientAddressLine;
	private String patientAddressCountry;
	private String patientTelecomNumber;
	private String encounterClass;
	private String encounterLocation;
	private String encounterPeriodStart;
	private String serviceID;
	private String serviceRequesterIdentifierValue;
	private String serviceRequesterDisplay;
	private String serviceAuthoredOn;
	private String serviceCodeCodingCode;
	private String serviceCodeText;
	private String serviceReasonCodeText;
	private String imagingIdentifier;
	private String imagingModalityCode;
}
