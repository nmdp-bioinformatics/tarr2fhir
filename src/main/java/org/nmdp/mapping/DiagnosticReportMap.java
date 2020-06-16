package org.nmdp.mapping;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Extension;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.gendxdatamodels.SampleXml;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticReportMap implements Converter<SampleXml, DiagnosticReport>{

    public DiagnosticReport convert(MappingContext<SampleXml, DiagnosticReport> context)
    {
        if (context.getSource() == null) {
            return null;
        }
        SampleXml aSampleXml = context.getSource();
        String aGlString  = aSampleXml.getLoci().getLocus().get(0).getTypingResult().getGLString();
        DiagnosticReport aDiagnosticReport = new DiagnosticReport();
        aDiagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

        Extension aExtension = new Extension();
        List<Extension> aExtensionList = new ArrayList<>();
        aExtension.setUrl("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/hla-genotype-summary");
        CodeableConcept aCodableConcept = new CodeableConcept();
        Coding aExtensionCoding = new Coding();
        aExtensionCoding.setSystem("http://glstring.org");
        aExtensionCoding.setCode(aGlString);
        List<Coding> aExtensionCodingList = new ArrayList<>();
        aExtensionCodingList.add(aExtensionCoding);
        aCodableConcept.setCoding(aExtensionCodingList);
        aExtension.setValue(aCodableConcept);
        aExtensionList.add(aExtension);
        aDiagnosticReport.setExtension(aExtensionList);

        CodeableConcept aDRCodableConcept = new CodeableConcept();
        List<Coding> aDRCodingList = new ArrayList<>();
        Coding aLoincCoding = new Coding();
        aLoincCoding.setSystem("http://loinc.org");
        aLoincCoding.setCode("81247-9");
        aLoincCoding.setDisplay("Master HL7 genetic variant reporting panel");
        Coding aGeneNameCoding = new Coding();
        aGeneNameCoding.setSystem("http://www.genenames.org/genegroup");
        aGeneNameCoding.setCode("588");
        aGeneNameCoding.setDisplay("Histocompatibility complex (HLA)");
        aDRCodingList.add(aLoincCoding);
        aDRCodingList.add(aGeneNameCoding);
        aDRCodableConcept.setCoding(aDRCodingList);
        aDiagnosticReport.setCode(aDRCodableConcept);

        return aDiagnosticReport;
    }
}
