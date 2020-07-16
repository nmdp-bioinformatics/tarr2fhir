package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Extension;

import java.util.ArrayList;
import java.util.List;

public class ExtensionSetup {
    private List<Extension> myExtensions;

    public List<Extension> getMyExtensions() {
        return myExtensions;
    }

    public void setMyExtensions(List<Extension> myExtensions) {
        this.myExtensions = myExtensions;
    }

    public ExtensionSetup()
    {
        myExtensions = new ArrayList<>();
    }

    public void createExtension(String theUrl, CodeableConcept theCodeableConcept)
    {
        Extension aExtension = new Extension();
        aExtension.setUrl(theUrl);
        aExtension.setValue(theCodeableConcept);
        myExtensions.add(aExtension);
    }
}
