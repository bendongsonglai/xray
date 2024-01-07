package inext.ris.order.xray.hapi.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.hapi.convert.FHIRtype;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Procedure;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ImagingDiagnosticService implements ImagingDiagnosticRepository {
    @Override
    public List<Procedure> getProcedureIdentifier(String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        List<Procedure> procedures = new ArrayList<>();
        try {
            Bundle bundle = client
                    .search()
                    .forResource(Procedure.class)
                    .where(Procedure.IDENTIFIER.exactly().identifier(identifier))
                    .returnBundle(Bundle.class)
                    .execute();

            for (int i = 0; i < bundle.getTotal(); i++) {
                procedures.add((Procedure) bundle.getEntry().get(i).getResource());
            }
        } catch (Exception e) {
            log.error("Get Procedure ImagingDiagnostic error!");
            e.printStackTrace();
        }
        return procedures;
    }

    @Override
    public Boolean checkProcedureIdentifier(String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        try {
            Bundle bundle = client
                    .search()
                    .forResource(Procedure.class)
                    .where(Procedure.IDENTIFIER.exactly().identifier(identifier))
                    .returnBundle(Bundle.class)
                    .execute();

            if (bundle.getTotal() > 0) {
                return true;
            }
        } catch (Exception e) {
            log.error("Check Procedure ImagingDiagnostic error!");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<DiagnosticReport> getDiagnosticReportIdentifier(String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        List<DiagnosticReport> reports = new ArrayList<>();
        try {
            Bundle bundle = client
                    .search()
                    .forResource(DiagnosticReport.class)
                    .where(Procedure.IDENTIFIER.exactly().identifier(identifier))
                    .returnBundle(Bundle.class)
                    .execute();
            for (int i = 0; i < bundle.getTotal(); i++) {
                reports.add((DiagnosticReport) bundle.getEntry().get(i).getResource());
            }
        } catch (Exception e) {
            log.error("Check DiagnosticReport ImagingDiagnostic error!");
            e.printStackTrace();
        }
        return reports;
    }
}
