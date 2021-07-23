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

import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProvenanceResource
{
    private final Provenance myProvenance;

    public Provenance getMyProvenance() {
        return myProvenance;
    }

    public ProvenanceResource()
    {
        myProvenance = new Provenance();
        myProvenance.setId(FhirGuid.genereateUrn());
    }

    public void generateProvenanceResource(List<String> theReferences)
    {
        theReferences.stream().filter(Objects::nonNull)
                .forEach(aRefString -> myProvenance.addTarget(new Reference(aRefString)));
    }

    public void setAgent(String theDeviceResource)
    {
        List<Provenance.ProvenanceAgentComponent> aProvAgents = new ArrayList<>();
        Provenance.ProvenanceAgentComponent aProvAgent = new Provenance.ProvenanceAgentComponent();
        aProvAgent.setWho(new Reference(theDeviceResource));
        aProvAgents.add(aProvAgent);
        myProvenance.setAgent(aProvAgents);
    }
}
