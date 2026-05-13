import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // ← AJOUTEZ CETTE LIGNE !
import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { MaterialModule } from './material.module';
import { SharedModule } from './shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ErrorInterceptor } from './components/ErrorInterceptor/ErrorInterceptor.component';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';




@NgModule({
  declarations: [
    AppComponent ,
    DashboardComponent,
    UserProfileComponent 
    
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule, // ← AJOUTEZ ICI !
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    SharedModule,
    MaterialModule,
     MatFormFieldModule,
    MatInputModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }