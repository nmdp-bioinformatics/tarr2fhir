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

import org.hl7.fhir.r4.model.Coding;

import java.util.ArrayList;
import java.util.List;

public class CodingSetup {

    private List<Coding> myCodingList;

    public List<Coding> getMyCodingList() {
        return myCodingList;
    }

    public void setMyCodingList(List<Coding> myCodingList) {
        this.myCodingList = myCodingList;
    }

    public CodingSetup()
    {
        myCodingList = new ArrayList<>();
    }

    public void addCoding(String theSystem, String theCode, String theDisplay)
    {
        Coding aCoding = new Coding();
        aCoding.setSystem(theSystem);
        aCoding.setCode(theCode);
        aCoding.setDisplay(theDisplay);
        myCodingList.add(aCoding);
    }

    public void addCoding(String theSystem, String theCode, String theVersion, String theDisplay)
    {
        Coding aCoding = new Coding();
        aCoding.setSystem(theSystem);
        aCoding.setVersion(theVersion);
        aCoding.setCode(theCode);
        aCoding.setDisplay(theDisplay);
        myCodingList.add(aCoding);
    }
}
