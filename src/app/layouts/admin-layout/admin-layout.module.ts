import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminLayoutRoutingModule } from './admin-layout.routing';
import { AdminLayoutComponent } from './admin-layout.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { MaterialModule } from '../../material.module';
import { SharedModule } from '../../shared/shared.module';
import { EmployeComponent } from './employe/employe.component';
import { FormsModule } from '@angular/forms';
import { DepartementComponent } from './departement/departement.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    AdminLayoutComponent,
    AdminDashboardComponent,
    EmployeComponent,
    DepartementComponent
    
    
  ],
  imports: [
    CommonModule,
    AdminLayoutRoutingModule,
    MaterialModule,
    SharedModule,
    FormsModule,
    RouterModule
  ]
})
export class AdminLayoutModule {}
