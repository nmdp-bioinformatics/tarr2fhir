package org.nmdp.tarrbean;

import org.nmdp.gendxdatamodels.LocusTARR;

import java.util.List;

public class SampleBean
{
    private String mySampleName;
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
