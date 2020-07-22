package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.*;

import java.util.List;
import java.util.Objects;

public class BundleResource
{
    public Bundle getMyFhirBundle() {
        return myFhirBundle;
    }
    private Bundle myFhirBundle;
    public void setMyFhirBundle(Bundle myFhirBundle)
    {
        this.myFhirBundle = myFhirBundle;
    }

    public BundleResource()
    {
        this.myFhirBundle = new Bundle();
        this.myFhirBundle.setType(Bundle.BundleType.TRANSACTION);
    }

    public void addDiagnosticReport(DiagnosticReport theDiagReport)
    {
        myFhirBundle.addEntry()
                .setFullUrl(theDiagReport.getIdElement().getValue())
                .setResource(theDiagReport)
                .getRequest()
                .setUrl("DiagnosticReport")
                .setMethod(Bundle.HTTPVerb.POST);
    }

    public void addObservations(List<Observation> theObservations)
    {
        theObservations.stream().filter(Objects::nonNull).forEach(aObs ->  myFhirBundle.addEntry()
                .setFullUrl(aObs.getIdElement().getValue())
                .setResource(aObs)
                .getRequest()
                .setUrl("Observation")
                .setMethod(Bundle.HTTPVerb.POST));
    }
    public void addPatient(Patient thePatient)
    {
        myFhirBundle.addEntry()
            .setFullUrl(thePatient.getIdElement().getValue())
            .setResource(thePatient)
            .getRequest()
            .setUrl("Patient")
            .setMethod(Bundle.HTTPVerb.POST);
    }

    public void addSpecimen(Specimen theSpecimen)
    {
        myFhirBundle.addEntry()
                .setFullUrl(theSpecimen.getIdElement().getValue())
                .setResource(theSpecimen)
                .getRequest()
                .setUrl("Specimen")
                .setMethod(Bundle.HTTPVerb.POST);
    }

    public void addProvenance(Provenance theProvenance)
    {
        myFhirBundle.addEntry()
                .setFullUrl(theProvenance.getIdElement().getValue())
                .setResource(theProvenance)
                .getRequest()
                .setUrl("Provenance")
                .setMethod(Bundle.HTTPVerb.POST);

    }
}
