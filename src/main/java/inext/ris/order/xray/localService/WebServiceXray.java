package inext.ris.order.xray.localService;


import inext.ris.order.xray.aes.AES;
import inext.ris.order.xray.clinicalElement.ClinicalElement;
import inext.ris.order.xray.code.CodeModel;
import inext.ris.order.xray.department.DepartmentModel;
import inext.ris.order.xray.doituong.DoituongModel;
import inext.ris.order.xray.hapi.Hapi;
import inext.ris.order.xray.hislog.HisLog;
import inext.ris.order.xray.hl7.country.Country;
import inext.ris.order.xray.hl7.msh.MSH;
import inext.ris.order.xray.hl7.obx.OBX;
import inext.ris.order.xray.hl7.race.Race;
import inext.ris.order.xray.hsm.HSMModel;
import inext.ris.order.xray.majorProblem.MajorProblem;
import inext.ris.order.xray.pacs.PacsModel;
import inext.ris.order.xray.patient_diagnosis.PatientDiagnosis;

import inext.ris.order.xray.patient_info.PatientInfoModel;
import inext.ris.order.xray.referrer.ReferrerModel;
import inext.ris.order.xray.report.Report;
import inext.ris.order.xray.report.ReportModel;
import inext.ris.order.xray.report.ReportPublic;
import inext.ris.order.xray.report.title.Title;
import inext.ris.order.xray.reportsigned.Signature;
import inext.ris.order.xray.request.RequestModel;
import inext.ris.order.xray.request_detail.RequestDetailModel;
import inext.ris.order.xray.signature.SignatureAccount;
import inext.ris.order.xray.type.TypeModel;
import inext.ris.order.xray.user.UserModel;
import inext.ris.order.xray.workstation.WSModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class WebServiceXray {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(6);
    private final WebClient localApiClient;

    @Autowired
    public WebServiceXray(WebClient localApiClient) {
        this.localApiClient = localApiClient;
    }

    public UserModel getUserByID(Long id) {
        Mono<UserModel> userMono = null;
        try {
            userMono = localApiClient.get()
                    .uri("/user/" + id)
                    .retrieve()
                    .bodyToMono(UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMono.block();
    }

    public UserModel getUserByLogin(String username) {
        Mono<UserModel> userMono = null;
        try {
            userMono = localApiClient.get()
                    .uri("/user/login?username=" + username)
                    .retrieve()
                    .bodyToMono(UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMono.block();
    }

    public UserModel getUserByCode(String code) {
        String encodeString = URLEncoder.encode(Base64.getEncoder().encodeToString(code.getBytes()));
        Mono<UserModel> userMono = null;
        try {
            userMono = localApiClient.get()
                    .uri("/user/code/" + encodeString)
                    .retrieve()
                    .bodyToMono(UserModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMono.block();
    }

    public RequestModel getRequestByAcc(String acc) {
        Mono<RequestModel> requestMono = null;
        try {
            requestMono = localApiClient.get()
                    .uri("/request/accession/" + acc)
                    .retrieve()
                    .onStatus(httpStatus-> !httpStatus.is4xxClientError(), response -> Mono.empty())
                    .onStatus(httpStatus-> !httpStatus.is5xxServerError(), response -> Mono.empty())
                    .bodyToMono(RequestModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestMono.block();
    }

    public Boolean checkRequestByAccAndMrn(String mrn, String acc) {
        Boolean check = false;
        try {
            check = localApiClient
                    .get()
                    .uri("/request/check/accession/" + mrn + "/" + acc)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(REQUEST_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public RequestDetailModel getRequestDetailsByAcc(String acc) {
        Mono<RequestDetailModel> requestDetailMono = null;
        try {
            requestDetailMono = localApiClient.get()
                    .uri("/requestdetail/accession/" + acc)
                    .retrieve()
                    .onStatus(httpStatus-> !httpStatus.is4xxClientError(), response -> Mono.empty())
                    .onStatus(httpStatus-> !httpStatus.is5xxServerError(), response -> Mono.empty())
                    .bodyToMono(RequestDetailModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return requestDetailMono.block();
    }

    public PatientInfoModel getPatientByMrn(String mrn) {
        Mono<PatientInfoModel> patientInfoMono = null;
        try {
            patientInfoMono = localApiClient.get()
                    .uri("/patient_info/mrn/" + mrn)
                    .retrieve()
                    .bodyToMono(PatientInfoModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientInfoMono.block();
    }

    public ReferrerModel getRefererrerByID(Long id) {
        Mono<ReferrerModel> refererrerMono = null;
        try {
            refererrerMono = localApiClient.get()
                    .uri("/referrer/" + id)
                    .retrieve()
                    .bodyToMono(ReferrerModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return refererrerMono.block();
    }

    public ReferrerModel getRefererrerByRefID(String ref_id) {
        Mono<ReferrerModel> refererrerMono = null;
        try {
            refererrerMono = localApiClient.get()
                    .uri("/referrer/ref/" + ref_id)
                    .retrieve()
                    .bodyToMono(ReferrerModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return refererrerMono.block();
    }

    public DepartmentModel getDepartmentBydepartID(String id) {
        Mono<DepartmentModel> departmentMono = null;
        try {
            departmentMono = localApiClient.get()
                    .uri("/department/depart?depart_id=" + id)
                    .retrieve()
                    .bodyToMono(DepartmentModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departmentMono.block();
    }

    public CodeModel getXrayCodeByCode(String code) {
        Mono<CodeModel> codeMono = null;
        try {
            codeMono = localApiClient.get()
                    .uri("/code/normal/" + code)
                    .retrieve()
                    .bodyToMono(CodeModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeMono.block();
    }

    public WSModel getWorkstation(String type) {
        Mono<WSModel> wsMono = null;
        try {
            wsMono = localApiClient.get()
                    .uri("/ws/type/" + type)
                    .retrieve()
                    .bodyToMono(WSModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wsMono.block();
    }

    public DoituongModel getDoituong(String type) {
        Mono<DoituongModel> doituongMono = null;
        try {
            doituongMono = localApiClient.get()
                    .uri("/doituong/type/" + type)
                    .retrieve()
                    .bodyToMono(DoituongModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doituongMono.block();
    }


    public Hapi getHapi() {
        Mono<Hapi> hapiMono = null;
        try {
            hapiMono = localApiClient.get()
                    .uri("/hapi/param")
                    .retrieve()
                    .bodyToMono(Hapi.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hapiMono.block();
    }

    public AES getAes() {
        Mono<AES> aesMono = null;
        try {
            aesMono = localApiClient.get()
                    .uri("/aes/param")
                    .retrieve()
                    .bodyToMono(AES.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aesMono.block();
    }

    public PacsModel getPacs() {
        Mono<PacsModel> pacsMono = null;
        try {
            pacsMono = localApiClient.get()
                    .uri("/pacs/1")
                    .retrieve()
                    .bodyToMono(PacsModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pacsMono.block();
    }

    public TypeModel getType(String modality) {
        Mono<TypeModel> typeMono = null;
        try {
            typeMono = localApiClient.get()
                    .uri("/type/get/" + modality)
                    .retrieve()
                    .bodyToMono(TypeModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeMono.block();
    }

    public void createSyncReport(String acc, Report report) {
        try {
            localApiClient.post()
                    .uri("/report/sync/" + acc)
                    .bodyValue(report)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(ReportModel.class))
                    .subscribe(rp -> System.out.println("Save report : " + rp.getId() + ", " + rp.getAccession()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createHisLog(HisLog hisLog) {
        try {
            localApiClient.post()
                    .uri("/hislog")
                    .bodyValue(hisLog)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(HisLog.class))
                    .subscribe(rp -> log.info("Save HisLog : " + rp.getId() + ", " + rp.getAccession()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createPatientDiagnosis(PatientDiagnosis patientDiagnosis) {
        try {
            localApiClient.post()
                    .uri("/patient_diagnosis")
                    .bodyValue(patientDiagnosis)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(PatientDiagnosis.class))
                    .subscribe(rp -> System.out.println("Save diagnosis : " + rp.getId() + ", " + rp.getMrn()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRequestDetail(RequestDetailModel requestDetailModel) {
        try {
            localApiClient.post()
                    .uri("/requestdetail/toreport/" + requestDetailModel.getAccession())
                    .bodyValue(requestDetailModel)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(ReportModel.class))
                    .subscribe(rp -> System.out.println("Update RequestDetail : " + rp.getId() + ", " + rp.getAccession()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Title getTitleReport(String xray_code) {
        Mono<Title> titleMono = null;
        try {
            titleMono = localApiClient.get()
                    .uri("/title/default/" + xray_code)
                    .retrieve()
                    .bodyToMono(Title.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titleMono.block();
    }

    public MSH getMSH() {
        Mono<MSH> mshMono = null;
        try {
            mshMono = localApiClient.get()
                    .uri("/msh/1")
                    .retrieve()
                    .bodyToMono(MSH.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mshMono.block();
    }

    public OBX getOBX() {
        Mono<OBX> obxMono = null;
        try {
            obxMono = localApiClient.get()
                    .uri("/obx/1")
                    .retrieve()
                    .bodyToMono(OBX.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obxMono.block();
    }

    public Country getCountry() {
        Mono<Country> countryMono = null;
        try {
            countryMono = localApiClient.get()
                    .uri("/terminology/country/default")
                    .retrieve()
                    .bodyToMono(Country.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryMono.block();
    }

    public Race getRace() {
        Mono<Race> raceMono = null;
        try {
            raceMono = localApiClient.get()
                    .uri("/terminology/race/default")
                    .retrieve()
                    .bodyToMono(Race.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return raceMono.block();
    }

    public ReportPublic getReportDetails(String acc) {
        Mono<ReportPublic> reportMono = null;
        try {
            reportMono = localApiClient.get()
                    .uri("/report/details?acc=" + acc)
                    .retrieve()
                    .bodyToMono(ReportPublic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportMono.block();
    }

    public ClinicalElement getCEByName(String name) {
        Mono<ClinicalElement> ceMono = null;
        String encodedStringCode = Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8));
        String encodeURL = "";
        try {
            encodeURL= URLEncoder.encode( encodedStringCode, "UTF-8" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ceMono = localApiClient.get()
                    .uri("/clinicalelement/name/"+encodeURL)
                    .retrieve()
                    .bodyToMono(ClinicalElement.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ceMono.block();
    }

    public MajorProblem getMPByName(String name) {
        Mono<MajorProblem> mpMono = null;
        String encodedStringCode = Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8));
        String encodeURL = "";
        try {
            encodeURL= URLEncoder.encode( encodedStringCode, "UTF-8" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mpMono = localApiClient.get()
                    .uri("/majorproblem/name/"+encodeURL)
                    .retrieve()
                    .bodyToMono(MajorProblem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mpMono.block();
    }

    public HSMModel getHSM() {
        Mono<HSMModel> hsmMono = null;
        try {
            hsmMono = localApiClient.get()
                    .uri("/hsm/1")
                    .retrieve()
                    .bodyToMono(HSMModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hsmMono.block();
    }

    public SignatureAccount getSignatureAccount(String userID) {
        Mono<SignatureAccount> signMono = null;
        try {
            signMono = localApiClient.get()
                    .uri("/signature/userid/"+userID)
                    .retrieve()
                    .bodyToMono(SignatureAccount.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signMono.block();
    }

    public List<String> getListBundleIDReport(String acc) {
        Mono<List<String>> bundlesMono = null;
        try {
            bundlesMono = localApiClient.get()
                    .uri("/fhir/listreport/"+acc)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundlesMono.block();
    }

    public void createHISLogs(HisLog hisLog) {
        try {
            localApiClient.post()
                    .uri("/hislog")
                    .bodyValue(hisLog)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(HisLog.class))
                    .subscribe(rp -> System.out.println("Save HIS Logs : " + rp.getId() + ", " + rp.getAccession()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createReportSignedPath(Signature signature) {
        try {
            localApiClient.post()
                    .uri("/signaturepath")
                    .bodyValue(signature)
                    .exchange()
                    .flatMap(res -> res.bodyToMono(Signature.class))
                    .subscribe(rp -> System.out.println("Save Report Signed Path : " + rp.getId() + ", " + rp.getAccession()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}