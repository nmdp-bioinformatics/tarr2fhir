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
