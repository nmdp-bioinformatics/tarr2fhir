package org.nmdp.controller;

import ca.uhn.fhir.context.FhirContext;
import io.swagger.annotations.ApiParam;
import io.swagger.api.hml.TarrApi;
import org.nmdp.fhirconversion.FhirGenerator;
import org.nmdp.gendxdatamodels.ProjectXml;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.xml.transform.StringSource;

import javax.validation.Valid;
import javax.xml.bind.JAXBContext;

@RestController
@RequestMapping("/tarr")
@CrossOrigin
public class TarrInputController implements TarrApi {

    @RequestMapping(path = "/convert2Fhir", headers="Accept=application/xml",  consumes = MediaType.APPLICATION_XML_VALUE, produces   = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> tarr2fhir(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String xml) {
      try {
          JAXBContext context = JAXBContext
                  .newInstance("org.nmdp.gendxdatamodels");
          FhirGenerator aFhirGenerator = new FhirGenerator();
          ProjectXml aProjXml = context.createUnmarshaller()
                  .unmarshal(new StringSource(xml), ProjectXml.class).getValue();

          aFhirGenerator.generateFhirBundle(aProjXml);

          FhirContext ctx = FhirContext.forR4();
          String aBundleDetails = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(aFhirGenerator.getMyBundleResource().getMyFhirBundle());
          return new ResponseEntity<>(aBundleDetails , HttpStatus.OK);
      }
      catch (Exception e)
      {
          return new ResponseEntity<>("Errors" , HttpStatus.INTERNAL_SERVER_ERROR);
      }
//        return new ResponseEntity<>("Not implemented" , HttpStatus.NOT_IMPLEMENTED);
    }
}
