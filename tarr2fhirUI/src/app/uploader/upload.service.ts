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

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private httpClient: HttpClient) {
    this.isDataReady = new BehaviorSubject<boolean>(false);
  }

  // URL for single file upload
  SERVER_URL_SINGLE = 'http://localhost:8090/tarr/convert2Fhir';

  // URL for zip file upload
  SERVER_URL_ZIP = 'http://localhost:8090/tarr/convertZip';

  // Observable boolean sources
  private isDataReady: BehaviorSubject<boolean>;

  data: JSON;

  getValue(): Observable<boolean> {
    return this.isDataReady.asObservable();
  }

  setValue(newValue): void {
    this.isDataReady.next(newValue);
  }

  public getData() {
    return this.data;
  }

  public upload(formData, uploaderComponent) {
    let headers = new HttpHeaders();

    if (uploaderComponent.choice === "zipFile")
    {
      let fD = new FormData();
      fD.append('upfile', formData, formData.name);
      fD.append('fileType', 'zip');
   //   headers = headers.set('Content-type', 'multipart/form-data; boundary=upfile');
      headers = headers.set('Access-Control-Request-Headers', '*');
      this.httpClient.post(this.SERVER_URL_ZIP, fD, {
        headers: headers
      })
        .subscribe((res) => {
        this.data = JSON.parse(JSON.stringify(res));
        console.log("Response received - " + JSON.stringify(res));
         uploaderComponent.announceDataReady(true);
     //   return this.data;
      });
    }
    else if (uploaderComponent.choice === "xmlFile") {
      headers = headers.set("Content-Type", "application/xml");
      this.httpClient.post(this.SERVER_URL_SINGLE, formData, {
        headers
      }).subscribe((res) => {
        this.data = JSON.parse(JSON.stringify(res));
        console.log("Response received - " + JSON.stringify(res));
        uploaderComponent.announceDataReady(true);
      });
      }
    return this.data;
  }
}
