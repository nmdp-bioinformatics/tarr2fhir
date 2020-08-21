import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {UploadService} from './upload.service';
import {saveAs} from 'file-saver';
import {Choice} from "./choice";

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

  announceDataReady(ready: boolean)
  {
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
   // const fileReader = new FileReader();
    const file = this.uploadForm.get('upfile').value;
    this.fileName = file.name;
    console.log("Filename = " + this.fileName);

    // fileReader.readAsText(file);
    // fileReader.onload = (e) => {
    //   console.log("reader = " + fileReader.result);
    // }
    this.choice = this.uploadForm.get('fileType').value;
    this.uploaderService.upload(file, this);
   //   this.announceDataReady(true);
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.uploadForm.get('upfile').setValue(file);
    }
  }

  // onRadioButton(event)
  // {
  //
  // }

  dataAvailable(isVisible : boolean)
   {
    this.isButtonVisible = isVisible;
   }

  displayAll()
  {
    let json = this.uploaderService.getData();
    this.saveToFileSystem(JSON.stringify(json));
  }

  private saveToFileSystem(response) {
    console.log("response received - \n"+response);
    let blob = new Blob([response]);
    saveAs(blob, "tarr_fhir_bundle.json");
  }

  ngOnDestroy() {
    this.uploaderService.setValue(false );
  }
}
