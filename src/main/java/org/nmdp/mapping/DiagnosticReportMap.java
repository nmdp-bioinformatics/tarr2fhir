package org.nmdp.mapping;

import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Meta;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.fhirsubmission.hapi.models.CodingSetup;
import org.nmdp.fhirsubmission.hapi.models.ExtensionSetup;
import org.nmdp.fhirsubmission.hapi.models.FhirGuid;
import org.nmdp.gendxdatamodels.LocusTARR;
import org.nmdp.tarrbean.SampleBean;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticReportMap implements Converter<SampleBean, DiagnosticReport>{

    public DiagnosticReport convert(MappingContext<SampleBean, DiagnosticReport> context)
    {
        if (context.getSource() == null) {
            return null;
        }
        SampleBean aSampleXml = context.getSource();
        DiagnosticReport aDiagnosticReport = new DiagnosticReport();
        aDiagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
        ExtensionSetup aExtensions = new ExtensionSetup();
        aSampleXml.getMyListLocusTarr().stream().forEach(aLocus -> createGlStringCodableConcept(aLocus, aExtensions));
        aDiagnosticReport.setExtension(aExtensions.getMyExtensions());

        CodeableConcept aDRCodableConcept = new CodeableConcept();
        CodingSetup aDRCodingSetup = new CodingSetup();
        aDRCodingSetup.addCoding("http://loinc.org", "81247-9", "Master HL7 genetic variant reporting panel");
        aDRCodingSetup.addCoding("http://www.genenames.org/genegroup","588","Histocompatibility complex (HLA)");
        aDRCodableConcept.setCoding(aDRCodingSetup.getMyCodingList());
        aDiagnosticReport.setCode(aDRCodableConcept);

        List<CanonicalType> aList = new ArrayList<>();
        CanonicalType aType = new CanonicalType();
        aType.setValueAsString("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/hla-summary-report");
        aList.add(aType);
        Meta aMeta = new Meta();
        aMeta.setProfile(aList);
        aDiagnosticReport.setMeta(aMeta);

        aDiagnosticReport.setId(FhirGuid.genereateUrn());
        return aDiagnosticReport;
    }

    public void createGlStringCodableConcept(LocusTARR theLocus, ExtensionSetup theExtensions)
    {
        String aGlString  = theLocus.getTypingResult().getGLString();
        String aLocusName = theLocus.getName();
        String aGlStringLocusName = aLocusName.substring(aLocusName.indexOf("-")+1);
        aGlString = aGlString.replaceAll(aGlStringLocusName, aLocusName);
        String aAlleleVersion = theLocus.getAlleleDB().getVersion();
        aAlleleVersion = aAlleleVersion.substring(aAlleleVersion.indexOf("HLA ") + 4);
        CodeableConcept aCodableConcept = new CodeableConcept();
        CodingSetup aCodingSetup = new CodingSetup();
        aCodingSetup.addCoding("http://glstring.org", "hla#"+aAlleleVersion+"#"+aGlString, "");
        aCodableConcept.setCoding(aCodingSetup.getMyCodingList());
        theExtensions.createExtension("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/hla-genotype-summary", aCodableConcept);
    }
}
