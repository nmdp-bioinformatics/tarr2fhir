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

package org.nmdp.fhirconversion;

import org.hl7.fhir.r4.model.*;
import org.modelmapper.ModelMapper;
import org.nmdp.fhirsubmission.hapi.models.*;
import org.nmdp.mapping.DiagnosticReportMap;
import org.nmdp.mapping.SpecimenMap;
import org.nmdp.tarrbean.SampleBean;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FhirGenerator
{
    private BundleResource myBundleResource;

    public void generateFhirBundle(SampleBean theSampleBean)
    {
        myBundleResource = new BundleResource();
        List<String> aProvenanceReferences = new ArrayList<>();

        /**
         * Generate Organization Resources from Meta-data input in UI
         */

        OrganizationResources aOrgResources = new OrganizationResources();
        if (!StringUtils.isEmpty(theSampleBean.getMyLabName()) && !theSampleBean.getMyLabName().equals("undefined"))
        {
            aOrgResources.generateOrganization("Lab (Performer)", theSampleBean.getMyLabName(), aProvenanceReferences);
        }

        if (!StringUtils.isEmpty(theSampleBean.getMyReportingCenter()) && !theSampleBean.getMyReportingCenter().equals("undefined"))
        {
            aOrgResources.generateOrganization("Reporting Organization ", theSampleBean.getMyReportingCenter(), aProvenanceReferences);
        }

        /*
         * Generate MolecularSequences from xml
         * */
        MolecularSequences aMS = new MolecularSequences();
        aMS.generateMolecularSequences(theSampleBean, aProvenanceReferences);

        ModelMapper mapper = createMapper();

        Observations aObss = new Observations();
        aObss.generateObservations(theSampleBean, aMS.getMyUUidGlStringMap(), aProvenanceReferences);
        List<Observation> observations = aObss.getMyObservations();
        List<Reference> theDiagReportReferences = new ArrayList<>();
        observations.stream().filter(Objects::nonNull)
                .filter(aObs -> aObs.getText().getDiv().getValue().contains("Genotype"))
                .forEach(obs -> getGenotypeReferences(theDiagReportReferences, obs.getId()));

        DiagnosticReport diagnosticReport = mapper.map(theSampleBean, DiagnosticReport.class);
        diagnosticReport.setText((new NarrativeText())
                .getNarrative("HLA genotyping report for sample name = " + theSampleBean.getMySampleName()));
        diagnosticReport.setResult(theDiagReportReferences);
        Identifier aSpecimenId = new Identifier();
        aSpecimenId.setSystem("http://terminology.cibmtr.org/identifier/tarr-sample-name");
        aSpecimenId.setValue(theSampleBean.getMySampleName());
        diagnosticReport.setSubject(new Reference().setIdentifier(aSpecimenId));
        aProvenanceReferences.add(diagnosticReport.getIdElement().getValue());

        Specimen aSpecimen = mapper.map(theSampleBean, Specimen.class);
        aProvenanceReferences.add(aSpecimen.getIdElement().getValue());

//        Patient aPatient = mapper.map(theSampleBean, Patient.class);
//        aProvenanceReferences.add(aPatient.getIdElement().getValue());

        DeviceResource aDR = new DeviceResource();
        aDR.generateDeviceResource(aProvenanceReferences);

        ProvenanceResource aPR = new ProvenanceResource();
        aPR.generateProvenanceResource(aProvenanceReferences);
        aPR.setAgent(aDR.getMyDevice().getIdElement().getValue());

        myBundleResource.addSequences(aMS.getMyMolecularSequences());
        myBundleResource.addResource(aSpecimen);
        //myBundleResource.addResource(aPatient);
        aOrgResources.getMyOrganizations().stream().filter(Objects::nonNull)
                .forEach(aOrg -> myBundleResource.addResource(aOrg));
//        myBundleResource.addResource(aReportingCenterOrganization.getMyOrganization());
        myBundleResource.addObservations(observations);
        myBundleResource.addResource(diagnosticReport);
        myBundleResource.addResource(aDR.getMyDevice());
        myBundleResource.addResource(aPR.getMyProvenance());
    }

    private void getGenotypeReferences(List<Reference> theReference, String theReferenceId)
    {
        Reference aRef = new Reference();
        aRef.setReference(theReferenceId);
        theReference.add(aRef);
    }

    public BundleResource getMyBundleResource() {
        return myBundleResource;
    }

    protected ModelMapper createMapper() {
        ModelMapper aMapper = new ModelMapper();
        aMapper.addConverter(new DiagnosticReportMap());
        aMapper.addConverter(new SpecimenMap());
        return aMapper;
    }
}
