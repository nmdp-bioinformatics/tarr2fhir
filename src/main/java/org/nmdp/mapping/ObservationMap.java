package org.nmdp.mapping;

import org.hl7.fhir.r4.model.*;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.fhirsubmission.hapi.models.CodingSetup;
import org.nmdp.fhirsubmission.hapi.models.FhirGuid;
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
        Observations aObss = new Observations();

        String locusName = locus.getName();
        String fullGlString = locus.getTypingResult().getGLString();
        String version = locus.getAlleleDB().getVersion();
        String[] glString = locus.getTypingResult().getGLString().split("\\+");

        Observation aAlleleObs1 = generateAlleleObservation(glString[0], locusName, version);
        Observation aAlleleObs2 = generateAlleleObservation(glString[1], locusName, version);
        Observation aGenotypeObs = generateGenotypeObservation(aAlleleObs1, aAlleleObs2, fullGlString, locusName, version);

        aObservationsList.add(aAlleleObs1);
        aObservationsList.add(aAlleleObs2);
        aObservationsList.add(aGenotypeObs);

        aObss.setMyObservations(aObservationsList);
        return aObss;
    }


    private Observation generateAlleleObservation(String theGlString, String theLocus, String theVersion)
    {
        Observation aAlleleObservation = new Observation();
        commonResourceEntries(aAlleleObservation, theLocus,"Allele");
        CodeableConcept aCodeCC = new CodeableConcept();
        List<CodeableConcept> aCodeCCList = new ArrayList<>();
        CodingSetup aCodingCC = new CodingSetup();
        aCodingCC.addCoding("http://loinc.org", "84414-2", "Haplotype name") ;
        aCodeCC.setCoding(aCodingCC.getMyCodingList());
        aCodeCCList.add(aCodeCC);
        aAlleleObservation.setCode(aCodeCC);
        aAlleleObservation.setValue(createGlStringCodeableConcept(theGlString, theLocus, theVersion));
        return aAlleleObservation;
    }

    private Observation generateGenotypeObservation(Observation theAlleleObs1, Observation theAlleleObs2, String theFullGlString, String theLocus, String theVersion)
    {
        Observation aGenotypeObservation = new Observation();
        commonResourceEntries(aGenotypeObservation, theLocus,"Genotype");

        CodeableConcept aCodeCC = new CodeableConcept();
        List<CodeableConcept> aCodeCCList = new ArrayList<>();
        CodingSetup aCodingCC = new CodingSetup();
        aCodingCC.addCoding("http://loinc.org", "84413-4", "Genotype display name") ;
        aCodeCC.setCoding(aCodingCC.getMyCodingList());
        aCodeCCList.add(aCodeCC);

        aGenotypeObservation.setCode(aCodeCC);
        aGenotypeObservation.setValue(createGlStringCodeableConcept(theFullGlString, theLocus, theVersion));

        List<Reference> aGenotypeObsReferences = new ArrayList<>();
        Reference aRef1 = new Reference(theAlleleObs1.getIdElement().getValue());
        Reference aRef2 = new Reference(theAlleleObs2.getIdElement().getValue());
        aGenotypeObsReferences.add(aRef1);
        aGenotypeObsReferences.add(aRef2);
        aGenotypeObservation.setDerivedFrom(aGenotypeObsReferences);

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
        theObs.setId(FhirGuid.genereateUrn());
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
        aCategoryCCList.add(aCategoryCC);
        theObs.setCategory(aCategoryCCList);

        theObs.setText(NarrativeText.getNarrative(theType + " Observation for " +theLocus));

        List<Observation.ObservationComponentComponent> aCompList = new ArrayList<>();
        Observation.ObservationComponentComponent aComp = new Observation.ObservationComponentComponent();

        CodeableConcept aHGNCCC = new CodeableConcept();
        CodingSetup aHgncCode = new CodingSetup();
        aHgncCode.addCoding("http://www.genenames.org/geneId", getHGNCCode(theLocus), theLocus);
        aHGNCCC.setCoding(aHgncCode.getMyCodingList());
        aComp.setValue(aHGNCCC);

        Observation.ObservationComponentComponent aComp2 = new Observation.ObservationComponentComponent();
        CodingSetup aLoincCode = new CodingSetup();
        aLoincCode.addCoding("\"http://loinc.org", getLoinc(theLocus), "");
        CodeableConcept aLoincCC = new CodeableConcept();
        aLoincCC.setCoding(aLoincCode.getMyCodingList());
        aComp2.setCode(aLoincCC);

        aCompList.add(aComp);
        aCompList.add(aComp2);
        theObs.setComponent(aCompList);
    }

    private CodeableConcept createGlStringCodeableConcept(String theGlString, String theLocus, String theVersion)
    {
        String aGlStringLocusName = theLocus.substring(theLocus.indexOf("-")+1);
        theVersion = theVersion.substring(theVersion.indexOf("HLA ") + 4);
        theGlString =  theGlString.replaceAll(aGlStringLocusName, theLocus);
        CodeableConcept aGlStringCC = new CodeableConcept();
        CodingSetup aValueCodingCC = new CodingSetup();
        aValueCodingCC.addCoding("http://glstring.org", "hla#" + theVersion + "#" + theGlString, "") ;
        aGlStringCC.setCoding(aValueCodingCC.getMyCodingList());
        return aGlStringCC;
    }

    private String getLoinc(String theLocusName)
    {
        switch(theLocusName)
        {
            case "HLA-A":
                return "57290-9";
            case "HLA-B":
                return "57291-7";
            case "HLA-C":
                return "77636-9";
            case "HLA-DPB1":
                return "59017-4";
            case "HLA-DQB1":
                return "57299-0";
            case "HLA-DRB1":
                return "57293-3";
            case "HLA-DRB3":
                return "57294-1";
            case "HLA-DRB4":
                return "57295-8";
            case "HLA-DRB5":
                return "57296-6";
            case "HLA-DPA1":
                return "59018-2";
            case "HLA-DQA1":
                return "59019-0";
        }
        return "";
    }

    private String getHGNCCode(String thelocusName)
    {
        switch (thelocusName)
        {
            case "HLA-A":
                return "HGNC:4931";
            case "HLA-B":
                return "HGNC:4932";
            case "HLA-C":
                return "HGNC:4933";
            case "HLA-DRB1":
                return "HGNC:4948";
            case "HLA-DQB1":
                return "HGNC:4944";
            case "HLA-DPB1":
                return "HGNC:4940";
            case "HLA-DPA1":
                return "HGNC:4938";
            case "HLA-DQA1":
                return "HGNC:4942";
            case "HLA-DRB3":
                return "HGNC:4951";
            case "HLA-DRB4":
                return "HGNC:4952";
            case "HLA-DRB5":
                return "HGNC:4953";
        }
        return null;
    }
}
