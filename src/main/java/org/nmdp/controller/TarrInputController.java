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

package org.nmdp.controller;

import io.swagger.annotations.ApiParam;
import io.swagger.api.hml.TarrApi;
import org.nmdp.fhirconversion.ParseInputFiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

@RestController
@RequestMapping("/tarr")
@CrossOrigin
public class TarrInputController implements TarrApi {

    @RequestMapping(path = "/convert2Fhir", headers="Accept=application/xml",  consumes = MediaType.APPLICATION_XML_VALUE, produces   = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> tarr2fhir(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String xml, @RequestParam String labName, @RequestParam String reportingCenter, @RequestParam String sampleType, @RequestParam String crid, @RequestParam String relationship) {
      try {
          ParseInputFiles aParser = new ParseInputFiles();
          List<String> aXmlInput = new ArrayList<>();
          aXmlInput.add(xml);
          aParser.setMyInputFiles(aXmlInput);
          aParser.setMetaData(labName, reportingCenter, sampleType, crid, relationship);
          aParser.process();
          if (aParser.getMyFhirBundleOutput() == null)
             return new ResponseEntity<>("Errors" , HttpStatus.PAYMENT_REQUIRED);
          return new ResponseEntity<>(aParser.getMyFhirBundleOutput() , HttpStatus.OK);
      }
      catch (Exception e)
      {
          return new ResponseEntity<>("Errors" , HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @RequestMapping(value = "/convertZip",
            produces = {"application/json"},
            headers = "Accept=*",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> tarr2fhirmulti(@ApiParam(value = "The file to upload.") @Valid @RequestPart(value="upfile", required=false) MultipartFile upfile, @RequestParam String labName, @RequestParam String reportingCenter, @RequestParam String sampleType,  @RequestParam String crid, @RequestParam String relationship) {
        try {
            MultipartFile aMPF = (MultipartFile)upfile;
            if (aMPF.isEmpty())
                return  new ResponseEntity<>("BAD INPUT" , HttpStatus.NO_CONTENT);

            ParseInputFiles aParser = new ParseInputFiles();
            ZipFile aZipFile = new ZipFile(convert(aMPF));
            aParser.unzipFile(aZipFile);
            aParser.setMetaData(labName, reportingCenter, sampleType, crid, relationship);
            aParser.process();
            return new ResponseEntity<>(aParser.getMyFhirBundleOutput(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Errors" , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Convert multipart zip file into regular file before extracting zip contents
     * @param file
     * @return
     * @throws Exception
     */
    public File convert(MultipartFile file) throws Exception
    {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
