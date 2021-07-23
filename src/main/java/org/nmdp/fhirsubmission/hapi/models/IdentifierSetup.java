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
        myIdentifiers = new ArrayList<>();
        if (thePatientType.equals("RECIPIENT"))
        {
            setCridIdentifier(theSampleId, thePatientType, theCenterCode);
        }
        setHmlSampleIdentifier(theSampleId, thePatientType, theCenterCode);
    }

    public IdentifierSetup()
    {
        myIdentifiers = new ArrayList<>();
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

    public void setHmlOrganizationIdentifier(String theSampleId, String thePatientType, String theCenterCode)
    {
        Identifier aSampleIdentifier = new Identifier();
        aSampleIdentifier.setSystem("http://terminology.nmdp.org/identifier/hml-sample");
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

    public void setOrganizationIdentifier(String theType, String theValue)
    {
        Identifier aSampleIdentifier = new Identifier();
        aSampleIdentifier.setSystem("http://terminology.nmdp.org/identifier/"+theType);
        aSampleIdentifier.setValue(theValue);

//        CodeableConcept aCodableConcept = new CodeableConcept();
//        Coding aCoding = new Coding();
//        aCoding.setSystem("http://terminology.cibmtr.org/codesystem/subject-type");
//        aCoding.setCode(thePatientType);
//        List<Coding> aCodingList = new ArrayList<>();
//        aCodingList.add(aCoding);
//        aCodableConcept.setCoding(aCodingList);
//        aSampleIdentifier.setType(aCodableConcept);
//
//        Extension aExtension = new Extension();
//        List<Extension> aExtensionList = new ArrayList<>();
//        aExtension.setUrl("http://hl7.org/fhir/StructureDefinition/rendered-value");
//        aExtension.addChild("valueString");
//        aExtension.setValue(new StringType(String.format("< sample id=%s center-code=%s >", theSampleId, theCenterCode)));
//        aExtensionList.add(aExtension);
//
//        aSampleIdentifier.setExtension(aExtensionList);
        myIdentifiers.add(aSampleIdentifier);
    }
}
