package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationResources
{
    private List<Organization> myOrganizations;

    public OrganizationResources(){myOrganizations = new ArrayList<>();
    }

    public List<Organization> getMyOrganizations() {
        return myOrganizations;
    }

    public void generateOrganization(String theType, String theIdentifer, List<String> theProvenanceReferences)
    {
        Organization aOrg = new Organization();
        aOrg.setText((new NarrativeText()).
            getNarrative("Organization: " +theType +" " + theIdentifer ));
        aOrg.setId(FhirGuid.genereateUrn());
        IdentifierSetup aIS = new IdentifierSetup();
        aIS.setOrganizationIdentifier(theType, theIdentifer);
        aOrg.setIdentifier(aIS.getIdentifiers());
        theProvenanceReferences.add(aOrg.getIdElement().getValue());
        myOrganizations.add(aOrg);
    }
}