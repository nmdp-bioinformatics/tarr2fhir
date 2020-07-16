package org.nmdp.fhirsubmission.hapi.models;

import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

public class NarrativeText
{
    public static Narrative getNarrative(String theResourceType, String aText1, String aText2)
    {
        Narrative aNarrative = new Narrative();
        XhtmlNode aXhtmlNode = new XhtmlNode();
        aXhtmlNode.setValue(String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\"> " + theResourceType + ": %s^%s</div>", aText1, aText2));
        aNarrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        aNarrative.setDiv(aXhtmlNode);
        return aNarrative;
    }

    public static Narrative getNarrative(String theDivString)
    {
        Narrative aNarrative = new Narrative();
        XhtmlNode aXhtmlNode = new XhtmlNode();
        aXhtmlNode.setValue(String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\"> " + theDivString));
        aNarrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        aNarrative.setDiv(aXhtmlNode);
        return aNarrative;
    }
}
