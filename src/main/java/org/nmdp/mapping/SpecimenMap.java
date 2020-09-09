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
        aPatientIdentifer.setSystem("http://terminology.cibmtr.org/identifier/tarr-sample-name");
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
