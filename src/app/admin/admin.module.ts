// src/app/admin/admin.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminRoutingModule } from './admin-routing.module';
import { EmployeListComponent } from './employe-list/employe-list.component';
import { EmployeFormComponent } from './employe-form/employe-form.component';

@NgModule({
  declarations: [
    EmployeListComponent,
    EmployeFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
