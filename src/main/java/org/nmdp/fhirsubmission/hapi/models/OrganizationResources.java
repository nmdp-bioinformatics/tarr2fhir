package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Organization;

import java.util.List;

public class OrganizationResources
{
    private Organization myOrganization;

    public OrganizationResources(){myOrganization = new Organization();}

    public Organization getMyOrganization() {
        return myOrganization;
    }

    public void generateOrganization(String theType, String theIdentifer, List<String> theProvenanceReferences)
    {
        myOrganization.setText((new NarrativeText()).
            getNarrative("Organization: " +theType +" " + theIdentifer ));
        myOrganization.setId(FhirGuid.genereateUrn());
        IdentifierSetup aIS = new IdentifierSetup();
        aIS.setOrganizationIdentifier(theType, theIdentifer);
        myOrganization.setIdentifier(aIS.getIdentifiers());
        theProvenanceReferences.add(myOrganization.getIdElement().getValue());
    }
}
