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
        theObservations.stream().filter(Objects::nonNull).map(aObs ->  myFhirBundle.addEntry()
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

//    public void generateFhirBundle(org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen spc, String hmlRootId, String hmlExtensionId, String thePatientType, String theReportingCenter)
//    {
//        myFhirBundle = new Bundle();
//        myFhirBundle.setType(Bundle.BundleType.TRANSACTION);
//
//        String sampleId = spc.getIdentifier().getValue();
//        String centerCode = spc.getIdentifier().getSystem();
//        String collectionMethod = spc.getCollection().getMethod();
//
//        Observations spcObs = spc.getObservations();
//        Observations sequenceObservations = handleObservations(spcObs);
//
//        Specimen aSpecimen = (new SpecimenResource()).getSpecimen(centerCode, sampleId, thePatientType, collectionMethod);
//        myFhirBundle.addEntry().setFullUrl(aSpecimen.getIdElement().getValue())
//                .setResource(aSpecimen)
//                .getRequest()
//                .setUrl("Specimen")
//                .setMethod(Bundle.HTTPVerb.POST)
//                .setIfNoneExist("identifier=http://nmdp.org/identifier/hml-sample|"+sampleId);
//    }
//
//    private Observations handleObservations(Observations theObservations)
//    {
//        Observations updatedObservations = new Observations();
//        List<Observation> aObsList = new ArrayList<>();
//
//        Map<String, List<Observation>> aMapObservations = new HashMap<>();
//        for (Observation obs : theObservations.getObservations())
//        {
//            List<Observation> aListObs = new ArrayList<>();
//            String locus = "";
//            if (obs.getGlstrings().getGlstrings() != null && obs.getGlstrings().getGlstrings().size() > 0) {
//                String glstring =  obs.getGlstrings().getGlstrings().get(0).getValue();
//                locus = glstring.substring(0, glstring.indexOf('*'));
//            }
//            else if (obs.getHaploids().getHaploids() != null && obs.getHaploids().getHaploids().size() > 0) {
//                locus = obs.getHaploids().getHaploids().get(0).getLocus();
//            }
//            aListObs.add(obs);
//            if (aMapObservations.containsKey(locus)) {
//                aMapObservations.get(locus).add(obs);
//            } else {
//                aMapObservations.put(locus, aListObs);
//            }
//        }
//
//        for (Map.Entry aEntry : aMapObservations.entrySet())
//        {
//            if (((List)aEntry.getValue()).size() == 2)
//            {
//                List<Observation> aOO = ((List)aEntry.getValue());
//                Object[] aO = aOO.toArray();
//                Observation obs = (Observation) aO[0];
//                Observation ob2 = (Observation) aO[1];
//                Haploid aHaploid1 = obs.getHaploids().getHaploids().get(0);
//                Haploid aHaploid2 = ob2.getHaploids().getHaploids().get(0);
//                String glString = aHaploid1.getLocus() + '*' + aHaploid1.getHaploidType() + "+" + aHaploid2.getLocus() + '*' + aHaploid2.getHaploidType();
//                Glstring gls = new Glstring();
//                gls.setValue(glString);
//                List<Glstring> aListGlS = new ArrayList<>();
//                Glstrings aGLS = new Glstrings();
//                aListGlS.add(gls);
//                aGLS.setGlstrings(aListGlS);
//                obs.setHaploids(new Haploids());
//                obs.setGlstrings(aGLS);
//                aObsList.add(obs);
//            }
//            else
//            {
//                aObsList.add((Observation) ((List) aEntry.getValue()).get(0));
//            }
//        }
//        updatedObservations.setObservations(aObsList);
//        return updatedObservations;
//    }
}
