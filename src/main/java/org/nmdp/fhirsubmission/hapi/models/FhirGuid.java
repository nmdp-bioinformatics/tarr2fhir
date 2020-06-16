package org.nmdp.fhirsubmission.hapi.models;

import java.util.UUID;

public class FhirGuid {

    public static String genereateUrn() {
        return String.format("urn:uuid:%s", UUID.randomUUID().toString());
    }
}
