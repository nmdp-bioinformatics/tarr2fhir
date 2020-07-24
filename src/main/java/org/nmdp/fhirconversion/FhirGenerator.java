package org.nmdp.fhirconversion;

import org.hl7.fhir.r4.model.*;
import org.modelmapper.ModelMapper;
import org.nmdp.fhirsubmission.hapi.models.*;
import org.nmdp.mapping.DiagnosticReportMap;
import org.nmdp.tarrbean.SampleBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FhirGenerator
{
    private BundleResource myBundleResource;
    private List<Observation> myObservations;
    private DiagnosticReport myDiagnosticReport;
    private Device myDevice;
    private Provenance myProvenance;



    public void generateFhirBundle(SampleBean theSampleBean)
    {
        myBundleResource = new BundleResource();
        List<String> aProvenanceReferences = new ArrayList<>();
        /*
        * Generate MolecularSequences from xml
        * */
        MolecularSequences aMS = new MolecularSequences();
        aMS.generateMolecularSequences(theSampleBean, aProvenanceReferences);
        List<MolecularSequence> molecularSequences = aMS.getMyMolecularSequences();

        ModelMapper mapper = createMapper();

//        List<Observation> observations = new ArrayList<>();
//        List<Observations> theObservations =
//        theSampleBean.getMyListLocusTarr().stream()
//                .filter(Objects::nonNull)
//                .map(aLocusTarr -> mapper.map(aLocusTarr, Observations.class))
//                .collect(Collectors.toList());
//
//
//
//        theObservations.stream().filter(Objects::nonNull)
//                .forEach(aObs -> makeObservation(aObs, observations, theSampleBean.getMySampleName()));

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


        DeviceResource aDR = new DeviceResource();
        aDR.generateDeviceResource(aProvenanceReferences);

        ProvenanceResource aPR = new ProvenanceResource();
        aPR.generateProvenanceResource(aProvenanceReferences);

        aProvenanceReferences.add(diagnosticReport.getIdElement().getValue());
        myBundleResource.addSequences(aMS.getMyMolecularSequences());
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


    public BundleResource getMyBundleResource()
    {
        return myBundleResource;
    }

    protected ModelMapper createMapper()
    {
        ModelMapper aMapper = new ModelMapper();
        aMapper.addConverter(new DiagnosticReportMap());
        return aMapper;
    }
}
