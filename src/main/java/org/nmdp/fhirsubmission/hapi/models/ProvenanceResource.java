package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;
import java.util.Objects;

public class ProvenanceResource
{
    private final Provenance myProvenance;

    public Provenance getMyProvenance() {
        return myProvenance;
    }

    public ProvenanceResource()
    {
        myProvenance = new Provenance();
        myProvenance.setId(FhirGuid.genereateUrn());
    }

    public void generateProvenanceResource(List<String> theReferences)
    {
        theReferences.stream().filter(Objects::nonNull)
                .forEach(aRefString -> myProvenance.addTarget(new Reference(aRefString)));
    }
}
