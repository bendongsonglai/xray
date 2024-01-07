package inext.ris.order.xray.hapi.service;

import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;

import java.util.List;

public interface EncounterRepository {
    public List<Encounter> getEncounterSubject(String subject);
    public List<Encounter> getEncounterIdentifier(String identifier);
    public Encounter getEncounterExactly(String subject, String identifier);
    public Coverage getCoverageSubject(String subject);
    public Coverage getCoverageIdentifier(String maql);
}
