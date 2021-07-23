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
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Meta;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.fhirsubmission.hapi.models.CodingSetup;
import org.nmdp.fhirsubmission.hapi.models.ExtensionSetup;
import org.nmdp.fhirsubmission.hapi.models.FhirGuid;
import org.nmdp.gendxdatamodels.LocusTARR;
import org.nmdp.tarrbean.SampleBean;
import org.springframework.util.StringUtils;

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
        StringBuilder aCombinedGlString = new StringBuilder();
        aCombinedGlString.append("hla#");
        String aAlleleVersion = aSampleXml.getMyListLocusTarr().get(0).getAlleleDB().getVersion();
//                theLocus.getAlleleDB().getVersion();
        aAlleleVersion = aAlleleVersion.substring(aAlleleVersion.indexOf("HLA ") + 4);
        aCombinedGlString.append(aAlleleVersion+"#");
        aSampleXml.getMyListLocusTarr().stream().forEach(aLocus -> createCombinedGlString(aLocus, aCombinedGlString));
        createGlStringCodableConcept(aCombinedGlString, aExtensions);

        if (!StringUtils.isEmpty(aSampleXml.getMySampleType()) && !aSampleXml.getMySampleType().equals("undefined"))
        {
            createExtensionCodeableConcept(aSampleXml.getMySampleType(), aExtensions);
        }
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

//        if (!StringUtils.isEmpty(aSampleXml.getMyCrid()) && !aSampleXml.getMyCrid().equals("undefined"))
//        {
//            createRecipientRefExtension(aExtensions, aSampleXml.getMyCrid());
//        }
        if (!StringUtils.isEmpty(aSampleXml.getMyRelationship()) && !aSampleXml.getMyRelationship().equals("undefined"))
        {
            createRecRelExtension(aExtensions, aSampleXml.getMyRelationship());
        }
        aDiagnosticReport.setId(FhirGuid.genereateUrn());
        return aDiagnosticReport;
    }

    public void createExtensionCodeableConcept(String theSampleType, ExtensionSetup theExtensions)
    {
        CodeableConcept aCodableConcept = new CodeableConcept();
        CodingSetup aCodingSetup = new CodingSetup();
        aCodingSetup.addCoding("http://terminology.cibmtr.org/codesystem/subject-type", theSampleType, "");
        aCodableConcept.setCoding(aCodingSetup.getMyCodingList());
        theExtensions.createExtension("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/subject-type-extension", aCodableConcept);
    }

    public void createGlStringCodableConcept(StringBuilder theCombinedGlString, ExtensionSetup theExtensions)
    {
//        String aGlString  = theLocus.getTypingResult().getGLString();
//        String aLocusName = theLocus.getName();
//        String aGlStringLocusName = aLocusName.substring(aLocusName.indexOf("-")+1);
//        aGlString = aGlString.replaceAll(aGlStringLocusName, aLocusName);
//        String aAlleleVersion = theLocus.getAlleleDB().getVersion();
//        aAlleleVersion = aAlleleVersion.substring(aAlleleVersion.indexOf("HLA ") + 4);
        CodeableConcept aCodableConcept = new CodeableConcept();
        CodingSetup aCodingSetup = new CodingSetup();
        String theFullGlString = theCombinedGlString.toString();
        aCodingSetup.addCoding("http://glstring.org", theFullGlString.substring(0, theFullGlString.length()-1), "");
        aCodableConcept.setCoding(aCodingSetup.getMyCodingList());
        theExtensions.createExtension("http://fhir.nmdp.org/ig/hla-reporting/StructureDefinition/hla-genotype-summary", aCodableConcept);
    }

    private void createCombinedGlString(LocusTARR theLocus, StringBuilder theCombinedGlString)
    {
        String aGlString  = theLocus.getTypingResult().getGLString();
        String aLocusName = theLocus.getName();
        String aGlStringLocusName = aLocusName.substring(aLocusName.indexOf("-")+1);
        aGlString = aGlString.replaceAll(aGlStringLocusName, aLocusName);
        theCombinedGlString.append(aGlString + "~");
    }

//    private void createRecipientRefExtension(ExtensionSetup theExtension, String theCrid)
//    {
//
//    }

    private void createRecRelExtension(ExtensionSetup theExtension, String theRel)
    {
        CodeableConcept aCodableConcept = new CodeableConcept();
        CodingSetup aCodingSetup = new CodingSetup();
        aCodingSetup.addCoding("http://cibmtr.org/codesystem/recipient-relationship", theRel, "");
        aCodableConcept.setCoding(aCodingSetup.getMyCodingList());
        theExtension.createExtension("http://fhir.nmdp.org/hla-reporting/StructureDefinition/recipient-relationship-extension", aCodableConcept);
    }
}
