package inext.ris.order.xray.hapi.service;

import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Procedure;

import java.util.List;

public interface ImagingDiagnosticRepository {
    public List<Procedure> getProcedureIdentifier(String identifier);
    public Boolean checkProcedureIdentifier(String identifier);
    public List<DiagnosticReport> getDiagnosticReportIdentifier(String identifier);
}
