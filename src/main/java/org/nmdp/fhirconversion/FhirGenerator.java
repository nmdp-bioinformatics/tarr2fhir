package org.nmdp.fhirconversion;

import org.hl7.fhir.r4.model.*;
import org.modelmapper.ModelMapper;
import org.nmdp.fhirsubmission.hapi.models.BundleResource;
import org.nmdp.fhirsubmission.hapi.models.MolecularSequences;
import org.nmdp.fhirsubmission.hapi.models.NarrativeText;
import org.nmdp.fhirsubmission.hapi.models.Observations;
import org.nmdp.mapping.*;
import org.nmdp.tarrbean.SampleBean;

import java.util.*;
import java.util.stream.Collectors;

public class FhirGenerator
{
    private BundleResource myBundleResource;
    public void generateFhirBundle(SampleBean aProjXml)
    {
        myBundleResource = new BundleResource();
        ModelMapper mapper = createMapper();

        List<MolecularSequence> sequences = new ArrayList<>();
        List<MolecularSequences> theSequences = aProjXml.getMyListLocusTarr().stream()
                .filter(Objects::nonNull)
                .map(aLocusTarr -> mapper.map(aLocusTarr, MolecularSequences.class))
                .collect(Collectors.toList());

        List<Observation> observations = new ArrayList<>();
        List<Observations> theObservations =
        aProjXml.getMyListLocusTarr().stream()
                .filter(Objects::nonNull)
                .map(aLocusTarr -> mapper.map(aLocusTarr, Observations.class))
                .collect(Collectors.toList());

        Map<String, String> aMap = new HashMap<>();
        theSequences.stream().filter(Objects::nonNull).forEach(theSeq -> makeSequences(theSeq, sequences));
        sequences.stream().filter(Objects::nonNull).
                forEach(seq -> addtoMap(seq, aMap));

        theObservations.stream().filter(Objects::nonNull).forEach(aObs -> makeObservation(aObs, observations, aProjXml.getMySampleName()));
//        observations.stream().filter(Objects::nonNull).forEach(obs->updateObsIds(obs, aProjXml.getMySampleName()));
        observations.stream().filter(Objects::nonNull).
                filter(obs-> obs.getText().getDiv().getValue().contains("Allele")).forEach(obs->updateReferences(obs, aMap));
//        observations.stream().filter(Objects::nonNull).forEach(obs-> update);
        DiagnosticReport diagnosticReport = mapper.map(aProjXml, DiagnosticReport.class);
        diagnosticReport.setText((new NarrativeText()).getNarrative("HLA genotyping report for sample name = " + aProjXml.getMySampleName()));
        List<Reference> theDiagReportReferences = new ArrayList<>();
        observations.stream().filter(Objects::nonNull).filter(aObs -> aObs.getId().contains("Genotype")).
                forEach(obs -> getGenotypeReferences(theDiagReportReferences, obs.getId()));
        diagnosticReport.setResult(theDiagReportReferences);

        myBundleResource.addSequences(sequences);
        myBundleResource.addObservations(observations);
        myBundleResource.addDiagnosticReport(diagnosticReport);

        Device aDevice = new Device();
        Device.DeviceDeviceNameComponent aDeviceName = new Device.DeviceDeviceNameComponent();
        List<Device.DeviceDeviceNameComponent> aDNameList = new ArrayList<>();
        aDeviceName.setName("Tarr2Fhir");
        aDeviceName.setType(Device.DeviceNameType.MODELNAME);
        aDNameList.add(aDeviceName);
        aDevice.setDeviceName(aDNameList);
        myBundleResource.addDevice(aDevice);


        Provenance aProvenance = new Provenance();
        aProvenance.addTarget(new Reference(diagnosticReport.getIdElement().getValue()));
        observations.stream().filter(Objects::nonNull)
                .forEach(obs -> aProvenance.addTarget(new Reference(obs.getIdElement().getValue())));
        aProvenance.addTarget(new Reference(aDevice.getIdElement().getValue()));
        sequences.stream().filter(Objects::nonNull)
                .forEach(seq -> aProvenance.addTarget(new Reference(seq.getIdElement().getValue())));
        myBundleResource.addProvenance(aProvenance);


    }

    private void addtoMap(MolecularSequence theSeq, Map<String, String> theMap)
    {
        String aGlString = theSeq.getReferenceSeq().getReferenceSeqId().getText();
//        if (theMap.get(aGlString) != null)
//        {
            theMap.put(aGlString, theSeq.getIdElement().getValue());
//        }
    }

    private void makeSequences(MolecularSequences theSeqss, List<MolecularSequence> theSeqs)
    {
        theSeqss.getMyMolecularSequences().stream().filter(Objects::nonNull).forEach(seq -> theSeqs.add(seq));
    }

    private void getGenotypeReferences(List<Reference> theReference, String theReferenceId)
    {
        Reference aRef = new Reference();
        aRef.setReference(theReferenceId);
        theReference.add(aRef);
    }

    public void makeObservation(Observations theObss, List<Observation> theObs, String theName)
    {
       theObss.getMyObservations().stream().filter(Objects::nonNull).forEach(obs -> theObs.add(obs));
    }

    public void updateReferences(Observation obs, Map<String, String> theMap)
    {
       List<Reference> aReferences = obs.getDerivedFrom();
       String aGlString = obs.getValueCodeableConcept().getCoding().get(0).getCode().substring(11);
       if (obs.getValueCodeableConcept().getCoding().get(0).getCode().contains(aGlString))
           aReferences.add(new Reference(theMap.get(aGlString)));
//       aReferences.stream().filter(Objects::nonNull).forEach(aRef -> renameReference(theMap.get(aGlString), aRef));
    }

//    public void renameReference(String theUUid, Reference theReference)
//    {
//       theReference.setReference(theUUid);
//    }

//    private void updateObsIds(Observation obs, String theName)
//    {
//        String aId = obs.getId();
//        int aIndex = aId.indexOf("tarr") + 4;
//        aId = aId.substring(0, aIndex) + theName+ aId.substring(aIndex);
//        obs.setId(aId);
//    }

    public BundleResource getMyBundleResource() {
        return myBundleResource;
    }

    protected ModelMapper createMapper()
    {
        ModelMapper aMapper = new ModelMapper();
        aMapper.addConverter(new PatientMap());
        aMapper.addConverter(new SpecimenMap());
        aMapper.addConverter(new DiagnosticReportMap());
        aMapper.addConverter(new ObservationMap());
        aMapper.addConverter(new MolecularSequenceMap());
        return aMapper;
    }
}
