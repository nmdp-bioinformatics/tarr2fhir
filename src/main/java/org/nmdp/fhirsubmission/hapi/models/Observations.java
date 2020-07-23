package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Observation;

import java.util.ArrayList;
import java.util.List;

public class Observations {

    private List<Observation> myObservations = new ArrayList<>();

    public List<Observation> getMyObservations() {
        return myObservations;
    }
    public void setMyObservations(List<Observation> myObservations) {
        this.myObservations = myObservations;
    }
}
