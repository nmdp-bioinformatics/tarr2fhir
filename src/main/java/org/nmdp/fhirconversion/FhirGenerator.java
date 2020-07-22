package org.nmdp.fhirconversion;

import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.modelmapper.ModelMapper;
import org.nmdp.fhirsubmission.hapi.models.BundleResource;
import org.nmdp.fhirsubmission.hapi.models.NarrativeText;
import org.nmdp.fhirsubmission.hapi.models.Observations;
import org.nmdp.mapping.DiagnosticReportMap;
import org.nmdp.mapping.ObservationMap;
import org.nmdp.mapping.PatientMap;
import org.nmdp.mapping.SpecimenMap;
import org.nmdp.tarrbean.SampleBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FhirGenerator
{
    private BundleResource myBundleResource;
    public void generateFhirBundle(SampleBean aProjXml)
    {
        myBundleResource = new BundleResource();
        ModelMapper mapper = createMapper();

        List<Observation> observations = new ArrayList<>();
        List<Observations> theObservations =
        aProjXml.getMyListLocusTarr().stream()
                .filter(Objects::nonNull)
                .map(aLocusTarr -> mapper.map(aLocusTarr, Observations.class))
                .collect(Collectors.toList());

        theObservations.stream().filter(Objects::nonNull).forEach(aObs -> makeObservation(aObs, observations, aProjXml.getMySampleName()));
        DiagnosticReport diagnosticReport = mapper.map(aProjXml, DiagnosticReport.class);
        diagnosticReport.setText((new NarrativeText()).getNarrative("HLA genotyping report for sample name = "+aProjXml.getMySampleName()));
        List<Reference> theDiagReportReferences = new ArrayList<>();
        observations.stream().filter(Objects::nonNull).filter(aObs -> aObs.getId().contains("Genotype")).
                forEach(obs -> getGenotypeReferences(theDiagReportReferences, obs.getId()));
        diagnosticReport.setResult(theDiagReportReferences);
        myBundleResource.addDiagnosticReport(diagnosticReport);
        myBundleResource.addObservations(observations);

        Provenance aProvenance = new Provenance();
        aProvenance.addTarget(new Reference().setReference("DiagnosticReport/" + diagnosticReport.getId()));
        observations.stream().filter(Objects::nonNull)
                .forEach(obs -> aProvenance.addTarget(new Reference().setReference("Observation/" + obs.getId())));
        myBundleResource.addProvenance(aProvenance);
    }

    private void getGenotypeReferences(List<Reference> theReference, String theReferenceId)
    {
        Reference aRef = new Reference();
        aRef.setReference("Observation/" + theReferenceId);
        theReference.add(aRef);
    }

    public void makeObservation(Observations theObss, List<Observation> theObs, String theName)
    {
       theObss.getMyObservations().stream().filter(Objects::nonNull).forEach(obs -> theObs.add(obs));
       theObs.stream().filter(Objects::nonNull).forEach(obs->updateObsIds(obs, theName));
       theObs.stream().filter(Objects::nonNull).forEach(obs->updateReferences(obs, theName));
    }

    public void updateReferences(Observation obs, String theName)
    {
       List<Reference> aReferences = obs.getDerivedFrom();
       aReferences.stream().filter(Objects::nonNull).forEach(aRef -> renameReference(theName, aRef));
    }

    public void renameReference(String theName, Reference theReference)
    {
        String aRefString = theReference.getReference();
        int aIndex = aRefString.indexOf("tarr") + 4;
        aRefString = aRefString.substring(0, aIndex) + theName+ aRefString.substring(aIndex);
        theReference.setReference(aRefString);
    }

    private void updateObsIds(Observation obs, String theName)
    {
        String aId = obs.getId();
        int aIndex = aId.indexOf("tarr") + 4;
        aId = aId.substring(0, aIndex) + theName+ aId.substring(aIndex);
        obs.setId(aId);
    }

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
        return aMapper;
    }
}
