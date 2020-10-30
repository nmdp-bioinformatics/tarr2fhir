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

package org.nmdp.tarrbean;

import org.nmdp.gendxdatamodels.LocusTARR;

import java.util.List;

public class SampleBean
{
    private String mySampleName;
    private String mySampleType;
    private String myLabName;
    private String myReportingCenter;

    public String getMySampleType() {
        return mySampleType;
    }

    public void setMySampleType(String mySampleType) {
        this.mySampleType = mySampleType;
    }

    public String getMyLabName() {
        return myLabName;
    }

    public void setMyLabName(String myLabName) {
        this.myLabName = myLabName;
    }

    public String getMyReportingCenter() {
        return myReportingCenter;
    }

    public void setMyReportingCenter(String myReportingCenter) {
        this.myReportingCenter = myReportingCenter;
    }

    private String myProjectName;
    private List<LocusTARR> myListLocusTarr;

    public String getMySampleName() {
        return mySampleName;
    }

    public void setMySampleName(String mySampleName) {
            this.mySampleName = mySampleName;
    }

    public String getMyProjectName() {
        return myProjectName;
    }

    public void setMyProjectName(String myProjectName) {
           this.myProjectName = myProjectName;
    }

    public List<LocusTARR> getMyListLocusTarr() {
        return myListLocusTarr;
    }

    public void setMyListLocusTarr(List<LocusTARR> myListLocusTarr) {
        this.myListLocusTarr = myListLocusTarr;
    }
}
