import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLayoutComponent } from './admin-layout.component';
import {DepartementComponent} from './departement/departement.component';
import {EmployeComponent} from './employe/employe.component';
import { CourrierEntrantComponent } from '../../components/courrier-entrant/courrier-entrant.component';

import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { DocumentComponent } from '../../components/document/document.component';

const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent, 
    children: [
      { path: '', redirectTo: 'employes', pathMatch: 'full' },
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'employes', component: EmployeComponent },
      { path: 'courriers', component: CourrierEntrantComponent },
      { path: 'documents', component: DocumentComponent },
      { path: 'departement', component: DepartementComponent }
      
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminLayoutRoutingModule { }