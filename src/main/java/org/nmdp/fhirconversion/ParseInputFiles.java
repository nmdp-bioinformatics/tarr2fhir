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

package org.nmdp.fhirconversion;

import ca.uhn.fhir.context.FhirContext;
import io.micrometer.core.instrument.util.IOUtils;
import org.nmdp.gendxdatamodels.LocusTARR;
import org.nmdp.gendxdatamodels.ProjectXml;
import org.nmdp.tarrbean.SampleBean;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.JAXBContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ParseInputFiles
{
    private String myFhirBundleOutput;

    public void setMyInputFiles(List<String> myInputFiles) {
        this.myInputFiles = myInputFiles;
    }

    private List<String> myInputFiles;
    private final SampleBean mySampleBean;
    private final List<LocusTARR> myLocusTarrList;
    private ProjectXml myProjectXml;

    public ParseInputFiles()
    {
        myInputFiles = new ArrayList<>();
        mySampleBean = new SampleBean();
        myLocusTarrList = new ArrayList<>();
        myProjectXml = new ProjectXml();
    }

    public String getMyFhirBundleOutput() {
        return myFhirBundleOutput;
    }

    public void processFiles(String xml)
    {
        parseTarrFile(xml);
        mySampleBean.setMyProjectName(myProjectXml.getProjectName());
        mySampleBean.setMySampleName(myProjectXml.getSamples().getSample().get(0).getName());
        myLocusTarrList.add(myProjectXml.getSamples().getSample().get(0).getLoci().getLocus().get(0));
    }

    public void parseTarrFile(String xml) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance("org.nmdp.gendxdatamodels");
            myProjectXml =  context.createUnmarshaller()
                    .unmarshal(new StringSource(xml), ProjectXml.class).getValue();
        } catch (Exception e) {

        }
    }

    public void setupFhir()
    {
        FhirGenerator aFhirGenerator = new FhirGenerator();
        aFhirGenerator.generateFhirBundle(mySampleBean);
        FhirContext ctx = FhirContext.forR4();
        myFhirBundleOutput = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(aFhirGenerator.getMyBundleResource().getMyFhirBundle());
    }

    public void unzipFile(ZipFile aZipFile)
    {
        Enumeration<? extends ZipEntry> aZipEntries = aZipFile.entries();
        while (aZipEntries.hasMoreElements())
        {
            ZipEntry aZE = aZipEntries.nextElement();
            try {
                InputStream aInputStream = aZipFile.getInputStream(aZE);
                String xml = IOUtils.toString(aInputStream, StandardCharsets.UTF_8);
                myInputFiles.add(xml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void process()
    {
        myInputFiles.stream().forEach(aXml -> processFiles(aXml));
        mySampleBean.setMyListLocusTarr(myLocusTarrList);
        setupFhir();
    }

    public void setMetaData(String theLabName, String theReportingCenter, String theSampleType, String crid, String relationship)
    {
        mySampleBean.setMyLabName(theLabName);
        mySampleBean.setMyReportingCenter(theReportingCenter);
        mySampleBean.setMySampleType(theSampleType);
        mySampleBean.setMyCrid(crid);
        mySampleBean.setMyRelationship(relationship);
    }

}
