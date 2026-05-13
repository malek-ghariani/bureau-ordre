import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResponsableLayoutComponent } from './responsable-layout.component';
import { ResponsableLayoutRoutingModule } from './responsable-layout.routing';
import { ResponsableDashboardComponent } from './responsable-dashboard/responsable-dashboard.component';
import { MaterialModule } from '../../material.module';
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    ResponsableLayoutComponent,
    ResponsableDashboardComponent,
    
    
  ],
  imports: [
    CommonModule,
    ResponsableLayoutRoutingModule,
    MaterialModule,
    SharedModule,
    RouterModule
  ]
})
export class ResponsableLayoutModule {}
