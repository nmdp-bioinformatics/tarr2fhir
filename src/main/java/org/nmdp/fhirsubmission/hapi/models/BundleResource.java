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

import java.util.List;
import java.util.Objects;

public class BundleResource
{
    public Bundle getMyFhirBundle() {
        return myFhirBundle;
    }
    private Bundle myFhirBundle;
    public void setMyFhirBundle(Bundle myFhirBundle)
    {
        this.myFhirBundle = myFhirBundle;
    }

    public BundleResource()
    {
        this.myFhirBundle = new Bundle();
        this.myFhirBundle.setType(Bundle.BundleType.TRANSACTION);
    }

    public void addResource(DomainResource theResource)
    {
        String theFullUrl = theResource.getIdElement().getValue();
        theResource.getIdElement().setValue(null);
        myFhirBundle.addEntry()
                .setFullUrl(theFullUrl)
                .setResource(theResource)
                .getRequest()
                .setUrl(theResource.getResourceType().toString())
                .setMethod(Bundle.HTTPVerb.POST);
    }

//    public void addDiagnosticReport(DiagnosticReport theDiagReport)
//    {
//        String theFullUrl = theDiagReport.getIdElement().getValue();
//        theDiagReport.getIdElement().setValue(null);
//        myFhirBundle.addEntry()
//                .setFullUrl(theFullUrl)
//                .setResource(theDiagReport)
//                .getRequest()
//                .setUrl("DiagnosticReport")
//                .setMethod(Bundle.HTTPVerb.POST);
//    }

    public void addObservations(List<Observation> theObservations)
    {
        theObservations.stream().filter(Objects::nonNull).forEach(aObs ->  addResource(aObs));
    }

//    public void addObstoBundle(Observation aObs)
//    {
//        String theFullUrl = aObs.getIdElement().getValue();
//        aObs.getIdElement().setValue(null);
//        myFhirBundle.addEntry()
//                .setFullUrl(theFullUrl)
//                .setResource(aObs)
//                .getRequest()
//                .setUrl("Observation")
//                .setMethod(Bundle.HTTPVerb.POST);
//    }
    public void addSequences(List<MolecularSequence> theSequences)
    {
        theSequences.stream().filter(Objects::nonNull).forEach(aSeq ->  addResource(aSeq));
    }

//    public void addSeqtoBundle(MolecularSequence theSeq)
//    {
//        String theFullUrl = theSeq.getIdElement().getValue();
//        theSeq.getIdElement().setValue(null);
//        myFhirBundle.addEntry()
//                .setFullUrl(theFullUrl)
//                .setResource(theSeq)
//                .getRequest()
//                .setUrl("MolecularSequence")
//                .setMethod(Bundle.HTTPVerb.POST);
//    }

//    public void addPatient(Patient thePatient)
//    {
//        myFhirBundle.addEntry()
//            .setFullUrl(thePatient.getIdElement().getValue())
//            .setResource(thePatient)
//            .getRequest()
//            .setUrl("Patient")
//            .setMethod(Bundle.HTTPVerb.POST);
//    }
//
//    public void addSpecimen(Specimen theSpecimen)
//    {
//        myFhirBundle.addEntry()
//                .setFullUrl(theSpecimen.getIdElement().getValue())
//                .setResource(theSpecimen)
//                .getRequest()
//                .setUrl("Specimen")
//                .setMethod(Bundle.HTTPVerb.POST);
//    }

//    public void addProvenance(Provenance theProvenance)
//    {
//        String theFullUrl = theProvenance.getIdElement().getValue();
//        theProvenance.getIdElement().setValue(null);
//        myFhirBundle.addEntry()
//                .setFullUrl(theFullUrl)
//                .setResource(theProvenance)
//                .getRequest()
//                .setUrl("Provenance")
//                .setMethod(Bundle.HTTPVerb.POST);
//
//    }

    public void addDevice(Device theDevice)
    {
        String theFullUrl = theDevice.getIdElement().getValue();
        theDevice.getIdElement().setValue(null);
        myFhirBundle.addEntry()
                .setFullUrl(theFullUrl)
                .setResource(theDevice)
                .getRequest()
                .setUrl("Device")
                .setMethod(Bundle.HTTPVerb.POST);

    }
}
