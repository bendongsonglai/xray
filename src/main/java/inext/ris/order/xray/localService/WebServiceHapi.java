package inext.ris.order.xray.localService;

import inext.ris.order.xray.his.model.ImagingData;
import inext.ris.order.xray.his.model.LocationData;
import inext.ris.order.xray.his.model.PractitionerData;
import inext.ris.order.xray.his.model.encounter.EncounterData;
import inext.ris.order.xray.his.model.patient.PatientData;
import inext.ris.order.xray.his.model.serviceRequest.ServiceRequestData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class WebServiceHapi {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(6);
    private final WebClient hapiApiClient;

    @Autowired
    public WebServiceHapi(WebClient hapiApiClient) {
        this.hapiApiClient = hapiApiClient;
    }

    public String createLocation(LocationData locationData) {
        String location = null;
        try {
            location = hapiApiClient
                    .post()
                    .uri("/location/convert" )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(locationData), LocationData.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    public String createEncounter(EncounterData encounterData) {
        String encounter = null;
        try {
            encounter = hapiApiClient
                    .post()
                    .uri("/encounter/convert" )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(encounterData), EncounterData.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encounter;
    }

    public String createServiceRequest(ServiceRequestData serviceRequestData) {
        String serviceRequest = null;
        try {
            serviceRequest = hapiApiClient
                    .post()
                    .uri("/servicerequest/convert" )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(serviceRequestData), ServiceRequestData.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceRequest;
    }

    public String createImagingStudy(ImagingData imagingData) {
        String imagingStudy = null;
        try {
            imagingStudy = hapiApiClient
                    .post()
                    .uri("/imaging/convert" )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(imagingData), ImagingData.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagingStudy;
    }

    public String createPractitioner(PractitionerData practitionerData) {
        String practitioner = null;
        try {
            practitioner = hapiApiClient
                    .post()
                    .uri("/practitioner/convert" )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(practitionerData), PractitionerData.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return practitioner;
    }

    public String createPatient(PatientData patientData) {
        String patient = null;
        try {
            patient = hapiApiClient
                    .post()
                    .uri("/patient/convert" )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(patientData), PatientData.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(REQUEST_TIMEOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }

    public Boolean checkLocation(String name) {
        Boolean check = false;
        try {
            check = hapiApiClient
                    .get()
                    .uri("/location/convert/check/" + name)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(REQUEST_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public Boolean checkPractitioner(String mabs) {
        Boolean check = false;
        try {
            check = hapiApiClient
                    .get()
                    .uri("/practitioner/convert/check?mabs=" + mabs)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(REQUEST_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }


}
