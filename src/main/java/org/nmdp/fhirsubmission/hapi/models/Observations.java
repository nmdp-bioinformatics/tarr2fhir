package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.modelmapper.ModelMapper;
import org.nmdp.mapping.ObservationMap;
import org.nmdp.tarrbean.SampleBean;

import java.util.*;
import java.util.stream.Collectors;

public class Observations {

    private List<Observation> myObservations = new ArrayList<>();

    public List<Observation> getMyObservations() {
        return myObservations;
    }
    public void setMyObservations(List<Observation> myObservations) {
        this.myObservations = myObservations;
    }

    public void generateObservations(SampleBean theSampleBean, Map<String, String> theUuidSequenceMap,  List<String> theProvenanceReferences)
    {
        ModelMapper aMapper = new ModelMapper();
        aMapper.addConverter(new ObservationMap());

//        List<Observation> observations = new ArrayList<>();
        List<Observations> theObservations =
                theSampleBean.getMyListLocusTarr().stream()
                        .filter(Objects::nonNull)
                        .map(aLocusTarr -> aMapper.map(aLocusTarr, Observations.class))
                        .collect(Collectors.toList());
        theObservations.stream().filter(Objects::nonNull)
                .forEach(aObs -> makeObservation(aObs, myObservations));
        myObservations.stream().filter(Objects::nonNull)
                .filter(obs-> obs.getText().getDiv().getValue().contains("Allele"))
                .forEach(obs->updateReferences(obs, theUuidSequenceMap));
        myObservations.stream().filter(Objects::nonNull).forEach(theSeq ->
                theProvenanceReferences.add(theSeq.getIdElement().getValue()));

        Identifier aSpecimenId = new Identifier();
        aSpecimenId.setSystem("http://terminology.cibmtr.org/identifier/tarr-sample-name");
        aSpecimenId.setValue(theSampleBean.getMySampleName());
        myObservations.stream().filter(Objects::nonNull).forEach(theObs -> theObs.setSubject(new Reference().setIdentifier(aSpecimenId)));
    }

    private void makeObservation(Observations theObss, List<Observation> theObs)
    {
        theObss.getMyObservations().stream().filter(Objects::nonNull).forEach(obs -> theObs.add(obs));
    }

    private void updateReferences(Observation obs, Map<String, String> theMap)
    {
        List<Reference> aReferences = obs.getDerivedFrom();
        String aGlString = obs.getValueCodeableConcept().getCoding().get(0).getCode().substring(11);
        Set<String> aReducedGlString = theMap.keySet();
        aReducedGlString.stream().filter(Objects::nonNull)
                .forEach(aGl -> mapReference(aGl, aGlString, aReferences, theMap));
        obs.setDerivedFrom(aReferences);
    }

    private void mapReference(String theGlFromMapKey, String theFullGlFromObs, List<Reference> aRefs, Map<String, String> theMap)
    {
        if (theFullGlFromObs.contains(theGlFromMapKey)) {
            aRefs.add(new Reference(theMap.get(theGlFromMapKey)));
        }
    }
}
