package inext.ris.order.xray.patient_info;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import inext.ris.order.xray.his.model.PeriodTime;
import inext.ris.order.xray.his.model.patient.*;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceHapi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/patient_info")
@Slf4j
@RequiredArgsConstructor
public class PatientInfoAPI {
    @Autowired
    PatientInfoService patientInfoService;

    @GetMapping
    public ResponseEntity<List<PatientInfoModel>> findAll() {
        return ResponseEntity.ok(patientInfoService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody PatientInfoModel patient) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = patientInfoService.existsByMRN(patient.getMrn()).compareTo(bi);
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceHapi webServiceHapi = new WebServiceHapi(webClientConfig.hapiApiClient());
        if (res > 0) {
            log.info("MRN " + patient.getMrn() + " is existed");
            PatientInfoModel patientInfo = patientInfoService.getPatientByMrn(patient.getMrn());
            try {
                String ssn = patient.getSsn();
                String mrn = patient.getMrn();
                String sex = patient.getSex();
                Date dob = patient.getBirth_Date();
                String tele = patient.getTelephone();
                String name = patient.getName();
                String lastname = patient.getLastName();
                String addr = patient.getAddress();
                String xn = patient.getXn();
                String name_old = patientInfo.getName();
                String lastname_old = patientInfo.getLastName();
                if ((sex != "") || (sex != null)) {
                    log.info("Update MRN {}", mrn);
                    patientInfoService.updatePatientInfoByMrn(ssn, mrn, sex, dob, tele, name,
                            lastname, addr, xn, name_old, lastname_old);
                }
                String patient_id = null;
                String phone=  null;
                String hoten=  null;
                String diachi=  null;
                String gender=  null;
                String birthDate=  null;
                PatientData patientData = new PatientData();
                try {
                    patient_id = mrn;
                    phone = patientInfo.getTelephone();
                    hoten = name;
                    diachi = patient.getAddress();
                    gender = patient.getSex();
                    birthDate = convertBirthDate(patient.getBirth_Date());
                    patientData.setOrganizationUUID("5097e79a-b980-4abd-961f-8baf3b91e837");
                    patientData.setPatientID(patient_id);
                    patientData.setNationalID(" ");
                    patientData.setPassPort(" ");
                    SocialBeneficiary socialBeneficiary = new SocialBeneficiary();
                    PeriodTime periodTime = new PeriodTime();
                    periodTime.setStart(null);
                    periodTime.setEnd(null);
                    socialBeneficiary.setPeriodTime(periodTime);
                    socialBeneficiary.setNumber("unknow");
                    patientData.setSocialBeneficiaryPeriod(socialBeneficiary.getPeriodTime());
                    patientData.setSocialBeneficiaryNumber(socialBeneficiary.getNumber());

                    List<ContactPointPatient> contactPointPatientList = new ArrayList<>();
                    ContactPointPatient contactPointPatient = new ContactPointPatient();
                    contactPointPatient.setSystem("PHONE");
                    contactPointPatient.setValue(phone);
                    contactPointPatient.setUse("HOME");
                    contactPointPatientList.add(contactPointPatient);
                    patientData.setTelecom(contactPointPatientList);

                    List<Extension> extensionList = new ArrayList<>();
                    Extension extensionEthic = new Extension();
                    extensionEthic.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-ethic");
                    extensionEthic.setCode("");
                    extensionEthic.setText("Không");

                    Extension extensionRace = new Extension();
                    extensionRace.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-race");
                    extensionRace.setCode("01");
                    extensionRace.setText("Kinh");

                    Extension extensionOccupation = new Extension();
                    extensionOccupation.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-occupation");
                    extensionOccupation.setCode("0000");
                    extensionOccupation.setText("Chưa biết");

                    Extension extensionNationality = new Extension();
                    extensionNationality.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-nationality");
                    extensionNationality.setCode("VN");
                    extensionNationality.setText("Việt Nam");

                    Extension extensionEducationLevel = new Extension();
                    extensionEducationLevel.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-education-level");
                    extensionEducationLevel.setCode("");
                    extensionEducationLevel.setText("Chưa biết");

                    extensionList.add(extensionEthic);
                    extensionList.add(extensionRace);
                    extensionList.add(extensionOccupation);
                    extensionList.add(extensionNationality);
                    extensionList.add(extensionEducationLevel);
                    patientData.setExtensionList(extensionList);

                    patientData.setPatientName(hoten);

                    AddressPatient addressPatient = new AddressPatient();

                    List<String> line1 = new ArrayList<>();
                    line1.add("Số nhà, " + " ,phường/xã ");
                    addressPatient.setLine(line1);
                    addressPatient.setText(diachi);


                    addressPatient.setCity("unknow");
                    addressPatient.setDistrict("unknow");
                    addressPatient.setCountry("Việt Nam");
                    patientData.setAddress(addressPatient);

                    if (gender == "M") {
                        patientData.setGender("male");
                    } else if (gender == "F") {
                        patientData.setGender("female");
                    } else {
                        patientData.setGender("other");
                    }
                    patientData.setBirthDate(birthDate);
                    String patientUUID = webServiceHapi.createPatient(patientData);
                    log.info("Patient FHIR update MRN {}", mrn);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("FHIR post Patient Error!");
                }

            } catch (Exception e){
                e.printStackTrace();
                log.error("Error update MRN {}", patient.getMrn());
            }
            return ResponseEntity.badRequest().build();
        }else {
            patientInfoService.save(patient);
            String ssn = patient.getSsn();
            String mrn = patient.getMrn();
            String sex = patient.getSex();
            Date dob = patient.getBirth_Date();
            String tele = patient.getTelephone();
            String name = patient.getName();
            String lastname = patient.getLastName();
            String addr = patient.getAddress();
            String xn = patient.getXn();
            String name_old = "";
            String lastname_old = "";
            patientInfoService.updatePatientInfoByMrn(ssn, mrn, sex, dob, tele, name,
                    lastname, addr, xn, name_old, lastname_old);
        }
        String patient_id = null;
        String phone=  null;
        String hoten=  null;
        String diachi=  null;
        String gender=  null;
        String birthDate=  null;
        PatientData patientData = new PatientData();
        try {
            patient_id = patient.getMrn();
            phone = patient.getTelephone();
            hoten = patient.getName();
            diachi = patient.getAddress();
            gender = patient.getSex();
            birthDate = convertBirthDate(patient.getBirth_Date());
            patientData.setOrganizationUUID("5097e79a-b980-4abd-961f-8baf3b91e837");
            patientData.setPatientID(patient_id);
            patientData.setNationalID(" ");
            patientData.setPassPort(" ");
            SocialBeneficiary socialBeneficiary = new SocialBeneficiary();
            PeriodTime periodTime = new PeriodTime();
            periodTime.setStart(null);
            periodTime.setEnd(null);
            socialBeneficiary.setPeriodTime(periodTime);
            socialBeneficiary.setNumber("unknow");
            patientData.setSocialBeneficiaryPeriod(socialBeneficiary.getPeriodTime());
            patientData.setSocialBeneficiaryNumber(socialBeneficiary.getNumber());

            List<ContactPointPatient> contactPointPatientList = new ArrayList<>();
            ContactPointPatient contactPointPatient = new ContactPointPatient();
            contactPointPatient.setSystem("PHONE");
            contactPointPatient.setValue(phone);
            contactPointPatient.setUse("HOME");
            contactPointPatientList.add(contactPointPatient);
            patientData.setTelecom(contactPointPatientList);

            List<Extension> extensionList = new ArrayList<>();
            Extension extensionEthic = new Extension();
            extensionEthic.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-ethic");
            extensionEthic.setCode("");
            extensionEthic.setText("Không");

            Extension extensionRace = new Extension();
            extensionRace.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-race");
            extensionRace.setCode("01");
            extensionRace.setText("Kinh");

            Extension extensionOccupation = new Extension();
            extensionOccupation.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-occupation");
            extensionOccupation.setCode("0000");
            extensionOccupation.setText("Chưa biết");

            Extension extensionNationality = new Extension();
            extensionNationality.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-nationality");
            extensionNationality.setCode("VN");
            extensionNationality.setText("Việt Nam");

            Extension extensionEducationLevel = new Extension();
            extensionEducationLevel.setSystem("http://moh.gov.vn/fhir/CodeSystem/vn-core-education-level");
            extensionEducationLevel.setCode("");
            extensionEducationLevel.setText("Chưa biết");

            extensionList.add(extensionEthic);
            extensionList.add(extensionRace);
            extensionList.add(extensionOccupation);
            extensionList.add(extensionNationality);
            extensionList.add(extensionEducationLevel);
            patientData.setExtensionList(extensionList);

            patientData.setPatientName(hoten);

            AddressPatient addressPatient = new AddressPatient();

            List<String> line1 = new ArrayList<>();
            line1.add("Số nhà, " + " ,phường/xã ");
            addressPatient.setLine(line1);
            addressPatient.setText(diachi);


            addressPatient.setCity("unknow");
            addressPatient.setDistrict("unknow");
            addressPatient.setCountry("Việt Nam");
            patientData.setAddress(addressPatient);

            if (gender == "M") {
                patientData.setGender("male");
            } else if (gender == "F") {
                patientData.setGender("female");
            } else {
                patientData.setGender("other");
            }
            patientData.setBirthDate(birthDate);
            String patientUUID = webServiceHapi.createPatient(patientData);
            log.info("Patient FHIR create MRN {}", patient_id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FHIR post Patient Error!");
        }
        PatientInfoModel patientInfo = patientInfoService.getPatientByMrn(patient.getMrn());
        return ResponseEntity.ok(patientInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientInfoModel> findById(@PathVariable Long id) {
        Optional<PatientInfoModel> patient = patientInfoService.findById(id);
        if (!patient.isPresent()) {
            log.error("Id " + id + " is not existed");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patient.get());
    }

    @GetMapping("/mrn/{mrn}")
    public ResponseEntity<PatientInfoModel> findByMrn(@PathVariable String mrn) {
        PatientInfoModel patient = patientInfoService.getPatientByMrn(mrn);
        if (patient.getMrn().isEmpty()) {
            log.error("Patient_info MRN " + mrn + " does not existed");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patient);
    }

    @RequestMapping(value = "/mrn/{mrn}", method = RequestMethod.POST)
    public void updatePatientByMrn(@PathVariable String mrn, @Valid @RequestBody PatientInfoModel patient) {
        PatientInfoModel patientInfo = patientInfoService.getPatientByMrn(mrn);
        if (patient.getMrn().isEmpty()) {
            log.error("Patient_info MRN " + mrn + " does not existed");
        }
        else {
            String ssn = patient.getSsn();
            String sex = patient.getSex();
            Date dob = patient.getBirth_Date();
            String tele = patient.getTelephone();
            String name = patient.getName();
            String lastname = patient.getLastName();
            String addr = patient.getAddress();
            String xn = patient.getXn();
            String name_old = patientInfo.getName();
            String lastname_old = patientInfo.getLastName();
            patientInfoService.updatePatientInfoByMrn(ssn, mrn, sex, dob, tele, name,
                    lastname, addr, xn, name_old, lastname_old);
        }
    }

    @RequestMapping(value = "/mrn/{mrn}/{ssn}", method = RequestMethod.PUT)
    public void updatePatientInfoByMrn(@PathVariable String mrn, @PathVariable String ssn) {
        patientInfoService.updatePatientByMrn(ssn, mrn);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PatientInfoModel> update(@PathVariable Long id, @Valid @RequestBody PatientInfoModel patient) {
        if (!patientInfoService.findById(id).isPresent()) {
            log.error("Patient_info Id " + id + " is not existed");
            return ResponseEntity.badRequest().build();
        }
        PatientInfoModel patientUpdate = patientInfoService.getOne(id);
        patientUpdate.setCenter_code(patient.getCenter_code());
        patientUpdate.setXn(patient.getXn());
        patientUpdate.setSsn(patient.getSsn());
        patientUpdate.setPrefix(patient.getPrefix());
        patientUpdate.setName(patient.getName());
        patientUpdate.setLastName(patient.getLastName());
        patientUpdate.setName_Eng(patient.getName_Eng());
        patientUpdate.setLastName_Eng(patient.getLastName_Eng());
        patientUpdate.setName_Old(patient.getName_Old());
        patientUpdate.setLastName_Old(patient.getLastName_Old());
        patientUpdate.setSex(patient.getSex());
        patientUpdate.setBirth_Date(patient.getBirth_Date());
        patientUpdate.setTelephone(patient.getTelephone());
        patientUpdate.setEmail(patient.getEmail());
        patientUpdate.setNote(patient.getNote());
        patientUpdate.setFirstVisitDate(patient.getFirstVisitDate());
        patientUpdate.setLastVisitDate(patient.getLastVisitDate());
        patientUpdate.setRight_Code(patient.getRight_Code());
        patientUpdate.setAddress(patient.getAddress());
        patientUpdate.setVillage(patient.getVillage());
        patientUpdate.setRoad(patient.getRoad());
        patientUpdate.setTambon_Code(patient.getTambon_Code());
        patientUpdate.setAmphoe_Code(patient.getAmphoe_Code());
        patientUpdate.setProvince_Code(patient.getProvince_Code());
        patientUpdate.setCountry_Code(patient.getCountry_Code());
        return ResponseEntity.ok(patientInfoService.save(patientUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!patientInfoService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            return ResponseEntity.badRequest().build();
        }

        patientInfoService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    private static String convertBirthDate(Date birthdate) {
        String dob = null;
        SimpleDateFormat newdf = new SimpleDateFormat("yyyyMMdd");
        birthdate  = addHoursToJavaUtilDate(birthdate, 17);
        try {
            dob = newdf.format(birthdate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dob;
    }
    private static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}
