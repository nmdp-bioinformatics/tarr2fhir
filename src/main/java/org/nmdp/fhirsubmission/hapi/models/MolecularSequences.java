package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.MolecularSequence;

import java.util.ArrayList;
import java.util.List;

public class MolecularSequences
{
    private List<MolecularSequence> myMolecularSequences = new ArrayList<>();

    public List<MolecularSequence> getMyMolecularSequences() {
        return myMolecularSequences;
    }
    public void setMyMolecularSequences(List<MolecularSequence> myMolecularSequences) {
        this.myMolecularSequences = myMolecularSequences;
    }
}
