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

package org.nmdp.mapping;

import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.fhirsubmission.hapi.models.CodingSetup;
import org.nmdp.fhirsubmission.hapi.models.FhirGuid;
import org.nmdp.fhirsubmission.hapi.models.MolecularSequences;
import org.nmdp.fhirsubmission.hapi.models.NarrativeText;
import org.nmdp.gendxdatamodels.AlleleSequence;
import org.nmdp.gendxdatamodels.AnnotatedAllele;
import org.nmdp.gendxdatamodels.LocusTARR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MolecularSequenceMap implements Converter<LocusTARR, MolecularSequences>
{
    public MolecularSequences convert(MappingContext<LocusTARR, MolecularSequences> context) {
        if (context.getSource() == null) {
            return null;
        }
        LocusTARR aLocusTarr = context.getSource();
        return setupMolecularSequenceResources(aLocusTarr);
    }

    private MolecularSequences setupMolecularSequenceResources(LocusTARR locus)
    {
        MolecularSequences aMSs = new MolecularSequences();
        String alleleVersion = locus.getAlleleDB().getVersion();
        List<MolecularSequence> aMSList = new ArrayList<>();
        List<AnnotatedAllele> annotatedAlleleList = locus.getAnnotatedAlleles() != null ? locus.getAnnotatedAlleles().getAnnotatedAllele() : new ArrayList<>();
        if (annotatedAlleleList.size() == 0)
            return aMSs;
        annotatedAlleleList.stream().filter(Objects::nonNull)
                .forEach(aAA -> extractSequence(aAA, aMSList, alleleVersion));
        aMSs.setMyMolecularSequences(aMSList);
        return aMSs;
    }

    private void extractSequence(AnnotatedAllele theAA, List<MolecularSequence> theMSList, String theVersion)
    {
        MolecularSequence aMS = new MolecularSequence();
        AlleleSequence aAS = theAA.getAlleleSequence();
        String theSeq = aAS.getValue();
        String theMatch = "HLA-" + aAS.getMatch();

        aMS.setId(FhirGuid.genereateUrn());

        List<CanonicalType> aList = new ArrayList<>();
        CanonicalType aType = new CanonicalType();
        aType.setValueAsString("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/hla-molecularsequence");
        aList.add(aType);
        Meta aMeta = new Meta();
        aMeta.setProfile(aList);
        aMS.setMeta(aMeta);

        aMS.setText(NarrativeText.getNarrative(theMatch));

        MolecularSequence.MolecularSequenceReferenceSeqComponent aMSR = new  MolecularSequence.MolecularSequenceReferenceSeqComponent();

        CodeableConcept aCodeCC = new CodeableConcept();
        List<CodeableConcept> aCodeCCList = new ArrayList<>();
        CodingSetup aCodingCC = new CodingSetup();
        aCodingCC.addCoding("http://www.ebi.ac.uk/ipd/imgt/hla", theMatch, theVersion, "") ;
        aCodeCC.setCoding(aCodingCC.getMyCodingList());
        aCodeCC.setText(theMatch);
        aCodeCCList.add(aCodeCC);

        aMSR.setReferenceSeqId(aCodeCC);

        aMS.setReferenceSeq(aMSR);
        aMS.setObservedSeq(theSeq);

        theMSList.add(aMS);
    }
}
