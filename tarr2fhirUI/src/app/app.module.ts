import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
// import { ValidatorsComponent } from './validators/validators.component';
import {UploaderComponent} from './uploader/uploader.component';

//import { ResponseDisplayComponent } from './response-display/response-display.component';

@NgModule({
  declarations: [
    AppComponent,
    // ValidatorsComponent,
    UploaderComponent
    //,
//    ResponseDisplayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
