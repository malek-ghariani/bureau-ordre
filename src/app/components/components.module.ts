import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';


import { CourrierEntrantComponent } from './courrier-entrant/courrier-entrant.component';
import { CourrierSortantComponent } from './courrier-sortant/courrier-sortant.component';


import { DocumentComponent } from './document/document.component';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { DefaultRedirectComponent } from './default-redirect/default-redirect.component';
import { DocumentListComponent } from './document/document-list/document-list.component';
import { DocumentFormComponent } from './document/document-form/document-form.component';
import { SendDocumentModalComponent } from './document/send-document-modal/send-document-modal.component';

import { ListePlanificationsComponent } from './planification/liste-planifications/liste-planifications.component';
import { FormulairePlanificationComponent } from './planification/formulaire-planification/formulaire-planification.component';
import { PlanificationComponent } from './planification/planification.component';
import { PlanificationEmployeComponent } from './planification/planification-employe/planification-employe.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule 
  ],
  declarations: [
    CourrierEntrantComponent,
    CourrierSortantComponent,
    
    
    DocumentComponent,
    LoginComponent,
    UnauthorizedComponent,
    DefaultRedirectComponent,
    DocumentListComponent,
    DocumentFormComponent,
    SendDocumentModalComponent,
    
    ListePlanificationsComponent,
    FormulairePlanificationComponent,
    PlanificationComponent,
    PlanificationEmployeComponent
  ],
  exports: [
    CourrierEntrantComponent,
    CourrierSortantComponent,
    
    
    DocumentComponent,
    LoginComponent
  ]
})
export class ComponentsModule { }
