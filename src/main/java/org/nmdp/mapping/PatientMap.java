package org.nmdp.mapping;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.nmdp.gendxdatamodels.SampleXml;

public class PatientMap implements Converter<SampleXml, Patient>
{
    public Patient convert(MappingContext<SampleXml, Patient> context) {
        if (context.getSource() == null) {
            return null;
        }
        Patient patient = new Patient();
        SampleXml aSampleXml = context.getSource();
        Identifier aPatientIdentifer = new Identifier();
        aPatientIdentifer.setSystem("http://cibmtr.org/identifier/tarr-sample-name");
        aPatientIdentifer.setValue(aSampleXml.getName());
        patient.addIdentifier(aPatientIdentifer);
        return patient;
    }
}
