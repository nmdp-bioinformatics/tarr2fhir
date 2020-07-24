package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.MolecularSequence;
import org.modelmapper.ModelMapper;
import org.nmdp.mapping.MolecularSequenceMap;
import org.nmdp.tarrbean.SampleBean;

import java.util.*;
import java.util.stream.Collectors;

public class MolecularSequences
{
    private List<MolecularSequence> myMolecularSequences = new ArrayList<>();
    private final Map<String, String> myUUidGlStringMap = new HashMap<>();

    public List<MolecularSequence> getMyMolecularSequences() {
        return myMolecularSequences;
    }
    public void setMyMolecularSequences(List<MolecularSequence> myMolecularSequences) {
        this.myMolecularSequences = myMolecularSequences;
    }

    public Map<String, String> getMyUUidGlStringMap() {
        return myUUidGlStringMap;
    }

    /**
     * Create the MolecularSequence resource (HAPI)
     * @param theSampleBean
     */
    public void generateMolecularSequences(SampleBean theSampleBean, List<String> theProvenanceReferences)
    {
        ModelMapper aMapper = new ModelMapper();
        aMapper.addConverter(new MolecularSequenceMap());

        myMolecularSequences = new ArrayList<>();
        List<MolecularSequences> theSequences = theSampleBean.getMyListLocusTarr().stream()
                .filter(Objects::nonNull)
                .map(aLocusTarr -> aMapper.map(aLocusTarr, MolecularSequences.class))
                .collect(Collectors.toList());
        theSequences.stream().filter(Objects::nonNull).forEach(theSeq -> makeSequences(theSeq, myMolecularSequences));
        getSequenceFhirUuid();
        myMolecularSequences.stream().filter(Objects::nonNull).forEach(theSeq ->
                theProvenanceReferences.add(theSeq.getIdElement().getValue()));
    }

    private void makeSequences(MolecularSequences theSeqss, List<MolecularSequence> theSeqs)
    {
        theSeqss.getMyMolecularSequences().stream().filter(Objects::nonNull).forEach(seq -> theSeqs.add(seq));
    }

    private void getSequenceFhirUuid()
    {
        myMolecularSequences.stream().filter(Objects::nonNull).
                forEach(seq -> addtoMap(seq, myUUidGlStringMap));
    }

    private void addtoMap(MolecularSequence theSeq, Map<String, String> theMap)
    {
        String aGlString = theSeq.getReferenceSeq().getReferenceSeqId().getText();
        theMap.put(aGlString, theSeq.getIdElement().getValue());
    }
}
