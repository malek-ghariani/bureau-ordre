import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResponsableLayoutComponent } from './responsable-layout.component';
import { ResponsableDashboardComponent } from './responsable-dashboard/responsable-dashboard.component';
import { DocumentComponent } from 'app/components/document/document.component';
import { ListePlanificationsComponent } from 'app/components/planification/liste-planifications/liste-planifications.component';
import { FormulairePlanificationComponent } from 'app/components/planification/formulaire-planification/formulaire-planification.component';

const routes: Routes = [
  {
    path: '',
    component: ResponsableLayoutComponent,
    children: [
      { path: '', redirectTo: 'documents', pathMatch: 'full' },
      { path: 'dashboard', component: ResponsableDashboardComponent },
       { path: 'documents', component: DocumentComponent },
       { path: 'planifications', component: ListePlanificationsComponent },
      { path: 'planifications/nouveau', component: FormulairePlanificationComponent },
      { path: 'planifications/:id', component: FormulairePlanificationComponent },
      
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResponsableLayoutRoutingModule {}
