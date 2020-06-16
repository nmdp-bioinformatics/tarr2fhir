package org.nmdp.mapping;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Specimen;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.nmdp.fhirsubmission.hapi.models.NarrativeText;
import org.nmdp.gendxdatamodels.SampleXml;


public class SpecimenMap implements Converter<SampleXml, Specimen>
{
    public Specimen convert(MappingContext<SampleXml, Specimen> context)
    {
        if (context.getSource() == null)
        {
            return null;
        }
        Specimen aSpecimen = new Specimen();
        SampleXml aSampleXml = context.getSource();
        ModelMapper mapper = createMapper();
        Identifier aPatientIdentifer = new Identifier();
        aPatientIdentifer.setSystem("http://cibmtr.org/identifier/tarr-sample-name");
        aPatientIdentifer.setValue(aSampleXml.getName());
        aSpecimen.addIdentifier(aPatientIdentifer);
        aSpecimen.setId(IdType.newRandomUuid());
        aSpecimen.setText(NarrativeText.getNarrative("Specimen", "versiti-code", aSampleXml.getName()));
        return aSpecimen;
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }
}
