package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.List;

public class IdentifierSetup
{
    private List<Identifier> myIdentifiers;
    public List<Identifier> getIdentifiers()
    {
        return myIdentifiers;
    }
    public IdentifierSetup(String theSampleId, String thePatientType, String theCenterCode)
    {
        if (thePatientType.equals("RECIPIENT"))
        {
            setCridIdentifier(theSampleId, thePatientType, theCenterCode);
        }
        setHmlSampleIdentifier(theSampleId, thePatientType, theCenterCode);
    }
    public void setHmlSampleIdentifier(String theSampleId, String thePatientType, String theCenterCode)
    {
        Identifier aSampleIdentifier = new Identifier();
        aSampleIdentifier.setSystem("http://versiti.org/vers-identifiers");
        aSampleIdentifier.setValue(theSampleId);

        CodeableConcept aCodableConcept = new CodeableConcept();
        Coding aCoding = new Coding();
        aCoding.setSystem("http://terminology.cibmtr.org/codesystem/subject-type");
        aCoding.setCode(thePatientType);
        List<Coding> aCodingList = new ArrayList<>();
        aCodingList.add(aCoding);
        aCodableConcept.setCoding(aCodingList);
        aSampleIdentifier.setType(aCodableConcept);

        Extension aExtension = new Extension();
        List<Extension> aExtensionList = new ArrayList<>();
        aExtension.setUrl("http://hl7.org/fhir/StructureDefinition/rendered-value");
        aExtension.addChild("valueString");
        aExtension.setValue(new StringType(String.format("< sample id=%s center-code=%s >", theSampleId, theCenterCode)));
        aExtensionList.add(aExtension);

        aSampleIdentifier.setExtension(aExtensionList);
        myIdentifiers.add(aSampleIdentifier);
    }

    public void setCridIdentifier(String theSampleId, String thePatientType, String theCenterCode)
    {

    }

    public void setOrganizationIdentifier(String theSampleId, String thePatientType, String theCenterCode)
    {
        Identifier aSampleIdentifier = new Identifier();
        aSampleIdentifier.setSystem("http://nmdp.org/identifier/hml-sample");
        aSampleIdentifier.setValue(theSampleId);

        CodeableConcept aCodableConcept = new CodeableConcept();
        Coding aCoding = new Coding();
        aCoding.setSystem("http://cibmtr.org/codesystem/subject-type");
        aCoding.setCode(thePatientType);
        List<Coding> aCodingList = new ArrayList<>();
        aCodingList.add(aCoding);
        aCodableConcept.setCoding(aCodingList);
        aSampleIdentifier.setType(aCodableConcept);

        Extension aExtension = new Extension();
        List<Extension> aExtensionList = new ArrayList<>();
        aExtension.setUrl("http://hl7.org/fhir/StructureDefinition/rendered-value");
        aExtension.addChild("valueString");
        aExtension.setValue(new StringType(String.format("< sample id=%s center-code=%s >", theSampleId, theCenterCode)));
        aExtensionList.add(aExtension);

        aSampleIdentifier.setExtension(aExtensionList);
        myIdentifiers.add(aSampleIdentifier);
    }
}
