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
@CrossOrigin(origins = "http://localhost:4200")
public class TarrInputController implements TarrApi {

    @RequestMapping(path = "/convert2Fhir", headers="Accept=application/xml",  consumes = MediaType.APPLICATION_XML_VALUE, produces   = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> tarr2fhir(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String xml) {
      try {
          ParseInputFiles aParser = new ParseInputFiles();
          List<String> aXmlInput = new ArrayList<>();
          aXmlInput.add(xml);
          aParser.setMyInputFiles(aXmlInput);
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
            headers = "Accept=*",
            method = RequestMethod.POST)
//    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseBody
    public ResponseEntity<String> tarr2fhirmulti(MultipartFile upfile) {
        try {
            MultipartFile aMPF = (MultipartFile)upfile;
            if (aMPF.isEmpty())
                return  new ResponseEntity<>("BAD INPUT" , HttpStatus.NO_CONTENT);

            ParseInputFiles aParser = new ParseInputFiles();
            ZipFile aZipFile = new ZipFile(convert(aMPF));
            aParser.unzipFile(aZipFile);
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
