package org.nmdp.fhirconversion;

import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Specimen;
import org.modelmapper.ModelMapper;
import org.nmdp.fhirsubmission.hapi.models.BundleResource;
import org.nmdp.gendxdatamodels.ProjectXml;
import org.nmdp.mapping.DiagnosticReportMap;
import org.nmdp.mapping.ObservationMap;
import org.nmdp.mapping.PatientMap;
import org.nmdp.mapping.SpecimenMap;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FhirGenerator
{
    private BundleResource myBundleResource;
    public void generateFhirBundle(ProjectXml aProjXml)
    {
        String theProjectName = aProjXml.getProjectName();
        myBundleResource = new BundleResource();
        ModelMapper mapper = createMapper();
        List<Patient> patients = aProjXml.getSamples().getSample().stream()
                .filter(Objects::nonNull)
                .map(aSample -> mapper.map(aSample, Patient.class))
                .collect(Collectors.toList());
        myBundleResource.addPatient(patients.get(0));
//        patients.stream().filter(Objects::nonNull).map(patient -> myBundleResource.addPatient(patient)).flatMap(List::stream).findAny();

        List<Specimen> specimens = aProjXml.getSamples().getSample().stream()
                .filter(Objects::nonNull)
                .map(aSample -> mapper.map(aSample, Specimen.class))
                .collect(Collectors.toList());
        myBundleResource.addSpecimen(specimens.get(0));
//        myBundleResource.addSpecimen((Specimen)specimens.stream().filter(Objects::nonNull).distinct());
        List<DiagnosticReport> diagnosticReports = aProjXml.getSamples().getSample().stream()
                .filter(Objects::nonNull)
                .map(aSample -> mapper.map(aSample, DiagnosticReport.class))
                .collect(Collectors.toList());
        myBundleResource.addDiagnosticReport(diagnosticReports.get(0));
        List<Observation> observations = aProjXml.getSamples().getSample().stream()
                .filter(Objects::nonNull)
                .map(aSample -> mapper.map(aSample.getLoci() , Observation.class))
                .collect(Collectors.toList());
        myBundleResource.addObservations(observations);
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
