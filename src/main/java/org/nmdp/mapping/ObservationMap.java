package org.nmdp.mapping;

import org.hl7.fhir.r4.model.*;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.fhirsubmission.hapi.models.CodingSetup;
import org.nmdp.fhirsubmission.hapi.models.NarrativeText;
import org.nmdp.fhirsubmission.hapi.models.Observations;
import org.nmdp.gendxdatamodels.LocusTARR;

import java.util.ArrayList;
import java.util.List;

public class ObservationMap implements Converter<LocusTARR, Observations>
{
    /**
     * @param context
     * @return
     */
    public Observations convert(MappingContext<LocusTARR, Observations> context) {
        if (context.getSource() == null) {
            return null;
        }

        Observations aObservations = new Observations();
        LocusTARR aLocusTarr = context.getSource();
        return setupObservationResource(aLocusTarr);
    }

    /**
     *
     * @param locus
     * @return
     */
    private Observations setupObservationResource(LocusTARR locus)
    {
        List<Observation> aObservationsList = new ArrayList<>();
        Observation aAlleleObs1 = new Observation();
        Observation aAlleleObs2 = new Observation();
        Observation aGenotypeObs = new Observation();
        Observations aObss = new Observations();

        String locusName = locus.getName();
        String fullGlString = locus.getTypingResult().getGLString();
        String[] glString = locus.getTypingResult().getGLString().split("\\+");

        aAlleleObs1 = generateAlleleObservation(glString[0], locusName, "1");
        aAlleleObs2 = generateAlleleObservation(glString[1], locusName, "2");
        aGenotypeObs = generateGenotypeObservation(aAlleleObs1, aAlleleObs2, fullGlString, locusName);

        aObservationsList.add(aAlleleObs1);
        aObservationsList.add(aAlleleObs2);
        aObservationsList.add(aGenotypeObs);
        aObss.setMyObservations(aObservationsList);

        return aObss;
    }


    private Observation generateAlleleObservation(String theGlString, String theLocus, String theNum)
    {
        Observation aAlleleObservation = new Observation();
        commonResourceEntries(aAlleleObservation, theLocus,"Allele-"+theNum);
        CodeableConcept aCodeCC = new CodeableConcept();
        List<CodeableConcept> aCodeCCList = new ArrayList<>();
        CodingSetup aCodingCC = new CodingSetup();
        aCodingCC.addCoding("http://loinc.org", "84414-2", "Haplotype name") ;
        aCodeCC.setCoding(aCodingCC.getMyCodingList());
        aCodeCCList.add(aCodeCC);
        aAlleleObservation.setCode(aCodeCC);

        CodeableConcept aValueCC = new CodeableConcept();
        List<CodeableConcept> aValueCCList = new ArrayList<>();
        CodingSetup aValueCodingCC = new CodingSetup();
        aValueCodingCC.addCoding("http://glstring.org", theGlString, "") ;
        aValueCC.setCoding(aValueCodingCC.getMyCodingList());
        aValueCCList.add(aValueCC);

        aAlleleObservation.setValue(aValueCC);

        return aAlleleObservation;
    }

    private Observation generateGenotypeObservation(Observation theAlleleObs1, Observation theAlleleObs2, String theFullGlString, String theLocus)
    {
        Observation aGenotypeObservation = new Observation();
        commonResourceEntries(aGenotypeObservation,theLocus,"Genotype");

        CodeableConcept aCodeCC = new CodeableConcept();
        List<CodeableConcept> aCodeCCList = new ArrayList<>();
        CodingSetup aCodingCC = new CodingSetup();
        aCodingCC.addCoding("http://loinc.org", "84413-4", "Genotype display name") ;
        aCodeCC.setCoding(aCodingCC.getMyCodingList());
        aCodeCCList.add(aCodeCC);
        aGenotypeObservation.setCode(aCodeCC);

        CodeableConcept aValueCC = new CodeableConcept();
        List<CodeableConcept> aValueCCList = new ArrayList<>();
        CodingSetup aValueCodingCC = new CodingSetup();
        aValueCodingCC.addCoding("http://glstring.org", theFullGlString, "") ;
        aValueCC.setCoding(aValueCodingCC.getMyCodingList());
        aValueCCList.add(aValueCC);

        aGenotypeObservation.setValue(aValueCC);
        List<Reference> aReferneces = new ArrayList<>();

        Reference aRef1 = new Reference();
        aRef1.setReference("Observation/"+theAlleleObs1.getId());
        Reference aRef2 = new Reference();
        aRef2.setReference("Observation/"+theAlleleObs2.getId());

        aReferneces.add(aRef1);
        aReferneces.add(aRef2);
        aGenotypeObservation.setDerivedFrom(aReferneces);

        return aGenotypeObservation;
    }

    /**
     *
     * @param theObs - Observation resource
     * @param theLocus - HLA locus
     * @param theType - Genotype or Allele
     */
    private void commonResourceEntries(Observation theObs, String theLocus, String theType)
    {
        theObs.setId("Versiti-tarr"+theLocus +"-" + theType);
        theObs.setStatus(Observation.ObservationStatus.FINAL);
        List<CanonicalType> aList = new ArrayList<>();
        CanonicalType aType = new CanonicalType();
        aType.setValueAsString("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/hla-"+theType.toLowerCase());
        aList.add(aType);
        Meta aMeta = new Meta();
        aMeta.setProfile(aList);
        theObs.setMeta(aMeta);

        CodeableConcept aCategoryCC = new CodeableConcept();
        List<CodeableConcept> aCategoryCCList = new ArrayList<>();
        CodingSetup aCodingSetup = new CodingSetup();
        aCodingSetup.addCoding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory", "");
        aCategoryCC.setCoding(aCodingSetup.getMyCodingList());
        theObs.setCategory(aCategoryCCList);

        NarrativeText narrativeText = new NarrativeText();
        theObs.setText(NarrativeText.getNarrative(theType + " Observation for " +theLocus));
    }
}
