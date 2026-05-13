// src/app/admin/admin-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeListComponent } from './employe-list/employe-list.component';
import { EmployeFormComponent } from './employe-form/employe-form.component';

const routes: Routes = [
  { path: 'employes', component: EmployeListComponent },
  { path: 'employes/new', component: EmployeFormComponent },
  { path: 'employes/:id', component: EmployeFormComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
