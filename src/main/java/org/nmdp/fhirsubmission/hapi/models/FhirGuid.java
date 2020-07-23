package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.IdType;

public class FhirGuid {
    public static IdType genereateUrn() {
        return IdType.newRandomUuid();
    }
}
