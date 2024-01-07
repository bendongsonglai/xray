package inext.ris.order.xray.hapi.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;

public class FHIRtype {

	public IGenericClient connectFHIR() {
		FhirContext ctx1 = new FhirContext(FhirVersionEnum.R4);
		ctx1.getRestfulClientFactory().setSocketTimeout(400 * 1000);
		//String serverBase =  "http://172.16.100.4:8081/iFHIR/baseR4";
		String serverBase =  "http://127.0.0.1:8081/iFHIR/baseR4";
		//String serverBase =  "http://192.168.101.11:8081/iFHIR/baseR4";
		//String serverBase =  "http://210.2.89.199:8080/iFHIRtest/baseR4";
		//String serverBase = "http://localhost:8080/baseR4";
		String username = "admin";
		String pass = "admin@fhir123";
		IGenericClient client = ctx1.newRestfulGenericClient(serverBase);
		IClientInterceptor interceptor = new BasicAuthInterceptor(username,pass);
		client.registerInterceptor(interceptor);
		return client;
	}


	public CodeableConcept code(String Sys, String codeValue, String text) {
		CodeableConcept code = new CodeableConcept();
		code.addCoding().setSystem(Sys).setCode(codeValue).setDisplay(text);
		code.setText(text);
		return code;
	}

	public CodeableConcept codeFull(String Sys, String codeValue, String display, String text) {
		CodeableConcept code = new CodeableConcept();
		code.addCoding().setSystem(Sys).setCode(codeValue).setDisplay(display);
		code.setText(text);
		return code;
	}

	public Coding coding(String Sys, String codeValue, String text) {
		Coding code = new Coding();
		code.setSystem(Sys);
		code.setCode(codeValue);
		code.setDisplay(text);
		return code;
	}

	public Period period(String start, String end) {
		Period period = new Period();
		Date startDate;
		Date endDate;
		
		if( start == null && end == null) {
			return null;
		}
		
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
			period.setStart(startDate);
			period.setEnd(endDate);
		} catch (ParseException e) {
			try {
				startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
				period.setStart(startDate);
				period.setEnd(endDate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				e.printStackTrace();
			}

		}

		return period;
	}

	public Period period(String start) {
		Period period = new Period();
		Date startDate;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
			period.setStart(startDate);
		} catch (ParseException e) {
			try {
				startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
				period.setStart(startDate);
			} catch (ParseException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}

		}

		return period;
	}

	public HumanName patientName(String patientData) {
		HumanName patientName = new HumanName();
		try {
			String[] Name = patientData.split("\\s", 2);
			String family = Name[0];
			String given = Name[1];

			String id1 = UUID.randomUUID().toString();
			patientName.setId(id1);
			patientName.setText(patientData);
			patientName.setFamily(family);
			patientName.addGiven(given);
			patientName.setUse(HumanName.NameUse.OFFICIAL);
		} catch (Exception e) {
			String id1 = UUID.randomUUID().toString();
			patientName.setId(id1);
			patientName.setText(patientData);
			patientName.setUse(HumanName.NameUse.OFFICIAL);
		}

		return patientName;
	}
	
	public Identifier identifier(String sysType, String codeType, String textType, String system, String Value) {
		FHIRtype FHIRtype = new FHIRtype();
		Identifier iden = new Identifier();
		iden.setUse(Identifier.IdentifierUse.OFFICIAL);
		String id1 = UUID.randomUUID().toString();
		iden.setId(id1);
		iden.setType(FHIRtype.code(sysType, codeType, textType));
		iden.setSystem(system);
		iden.setValue(Value);
		return iden;
	}
	
	public Extension extensionPatient(String url, String sys, String code, String text) {
		Extension extensionPatient = new Extension();
		FHIRtype FHIRtype = new FHIRtype();
		extensionPatient.setUrl(url);
		extensionPatient.setValue(FHIRtype.code(sys, code, text));
		return extensionPatient;
	}

}
