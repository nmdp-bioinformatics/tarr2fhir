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

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {UploadService} from './upload.service';
import {saveAs} from 'file-saver';
import {Choice} from './choice';

@Component({
  selector: 'app-uploader',
  templateUrl: './uploader.component.html',
  styleUrls: ['./uploader.component.scss'],
  providers: [UploadService]
})

export class UploaderComponent implements OnInit {

  uploadForm: FormGroup;
  response: Response;
  private fileName;
  isButtonVisible = false;
  choice: Choice;


  constructor(private uploaderService: UploadService, private formBuilder: FormBuilder) {
  }

  announceDataReady(ready: boolean) {
    this.uploaderService.setValue(ready);
  }

  ngOnInit() {
    this.uploadForm = this.formBuilder.group({
      upfile: [''],
      fileType: new FormControl('')
    });
    this.uploaderService.getValue().subscribe((value) => {
      this.isButtonVisible = value;
    });
  }

  data : string;

  onSubmit() {
    const file = this.uploadForm.get('upfile').value;
    this.fileName = file.name;
    console.log("Filename = " + this.fileName);
    this.choice = this.uploadForm.get('fileType').value;
    this.uploaderService.upload(file, this);
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.uploadForm.get('upfile').setValue(file);
    }
  }

  dataAvailable(isVisible : boolean) {
    this.isButtonVisible = isVisible;
   }

  displayAll() {
    let json = this.uploaderService.getData();
    this.saveToFileSystem(JSON.stringify(json));
  }

  private saveToFileSystem(response) {
    console.log("response received - \n"+response);
    let blob = new Blob([response]);
    saveAs(blob, "tarr_fhir_bundle.json");
    this.refreshPage();
  }

  refreshPage() {
    window.location.reload();
  }

  ngOnDestroy() {
    this.uploaderService.setValue(false );
  }
}
