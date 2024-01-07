package inext.ris.order.xray.hapi.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import inext.ris.order.xray.hapi.convert.FHIRtype;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EncounterService implements EncounterRepository{
    @Override
    public List<Encounter> getEncounterSubject(String subject) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        List<Encounter> encounters = new ArrayList<Encounter>();
        try {
            Bundle bundles = client
                    .search()
                    .forResource(Encounter.class)
                    .where(Encounter.SUBJECT.hasAnyOfIds(subject))
                    .returnBundle(Bundle.class)
                    .execute();
            for (int i = 0; i > bundles.getTotal(); i++) {
                encounters.add((Encounter) bundles.getEntry().get(i).getResource());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get Encounter Subject {} error!", subject);
        }
        return encounters;
    }

    @Override
    public List<Encounter> getEncounterIdentifier(String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        log.info("Get Encounter ID {}",identifier);

        Bundle bundles = client
                .search()
                .forResource(Encounter.class)
                .where(Encounter.IDENTIFIER.exactly().identifier(identifier))
                .returnBundle(Bundle.class)
                .execute();
        log.info("Encounter Exactly bundles {}",new FhirContext(FhirVersionEnum.R4).newJsonParser().setPrettyPrint(true).encodeResourceToString(bundles));
        List<Encounter> listEncouter = new ArrayList<Encounter>();
        if (bundles.getTotal() < 1) {
            return listEncouter;
        }

        for (int i = 0 ; i < bundles.getEntry().size() ; i++) {
            Encounter encounter = (Encounter) bundles.getEntry().get(i).getResource();
            listEncouter.add(encounter );
        }

        while (bundles.getLink(IBaseBundle.LINK_NEXT) != null) {
            bundles = client
                    .loadPage()
                    .next(bundles)
                    .execute();
            for (int i = 0 ; i < bundles.getEntry().size() ; i++) {
                Encounter encounter = (Encounter) bundles.getEntry().get(i).getResource();
                listEncouter.add(encounter );
            }
        }
        return listEncouter;
    }

    @Override
    public Encounter getEncounterExactly(String subject, String identifier) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Encounter encounter = new Encounter();

        Bundle bundle = client
                .search()
                .forResource(Encounter.class)
                .where(Encounter.SUBJECT.hasAnyOfIds(subject))
                .where(Encounter.IDENTIFIER.exactly().identifier(identifier))

                .returnBundle(Bundle.class)
                .execute();

        log.info("Encounter Exactly bundle {}",new FhirContext(FhirVersionEnum.R4).newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
        if (bundle.getTotal() < 1) {
            return null;
        }
        encounter = (Encounter) bundle.getEntry().get(0).getResource();
        log.info("Encounter Exactly IDPart {}",new FhirContext(FhirVersionEnum.R4).newJsonParser().setPrettyPrint(true).getEncodeForceResourceId().getIdPart());
        return encounter;
    }

    @Override
    public Coverage getCoverageSubject(String subject) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Coverage coverage = new Coverage();
        log.info("Encounter Coverage Subject {}",subject);
        Bundle bundle = client
                .search()
                .forResource(Coverage.class)
                .where(Coverage.POLICY_HOLDER.hasAnyOfIds(subject))
                .returnBundle(Bundle.class)
                .execute();

        if (bundle.getTotal() < 1) {
            return null;
        }
        coverage = (Coverage) bundle.getEntry().get(0).getResource();
        log.info("Encounter Coverage {}",coverage);
        return coverage;
    }

    @Override
    public Coverage getCoverageIdentifier(String maql) {
        FHIRtype FHIRtype = new FHIRtype();
        IGenericClient client = FHIRtype.connectFHIR();
        Coverage coverage = new Coverage();
        log.info("Encounter Coverage Identifier Maql {}",maql);
        Bundle bundle = client
                .search()
                .forResource(Coverage.class)
                .where(Coverage.IDENTIFIER.exactly().identifier(maql))
                .returnBundle(Bundle.class)
                .execute();

        if (bundle.getTotal() < 1) {
            return null;
        }
        coverage = (Coverage) bundle.getEntry().get(0).getResource();
        log.info("Encounter Coverage {}",coverage);
        return coverage;
    }
}
