/*
 * Copyright (c) 2020 Be The Match operated by National Marrow Donor Program (NMDP).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

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
