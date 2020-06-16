package org.nmdp.mapping;

import org.hl7.fhir.r4.model.Observation;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.nmdp.gendxdatamodels.ArrayOfLocusTARR;
import org.nmdp.gendxdatamodels.LocusTARR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObservationMap implements Converter<ArrayOfLocusTARR, Observation>
{
    public Observation convert(MappingContext<ArrayOfLocusTARR, Observation> context) {
        if (context.getSource() == null) {
            return null;
        }
        Observation aObs = new Observation();
        List<Observation> obs = new ArrayList<>();
        ArrayOfLocusTARR arrayOfLocusTARR = context.getSource();
        obs = arrayOfLocusTARR.getLocus().stream().filter(Objects::nonNull)
                .map(aLocus -> setupObservationResource(aLocus)).collect(Collectors.toList());
        return aObs;
    }

    private Observation setupObservationResource(LocusTARR locus)
    {
        Observation aObs = new Observation();

        String locusName = locus.getName();
        String geneName = locus.getAlleleDB().getGene();
        String alleleVersion = locus.getAlleleDB().getVersion();


        return aObs;
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }
}
