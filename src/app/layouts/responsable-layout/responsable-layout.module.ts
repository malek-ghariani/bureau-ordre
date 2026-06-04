import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResponsableLayoutComponent } from './responsable-layout.component';
import { ResponsableLayoutRoutingModule } from './responsable-layout.routing';
import { ResponsableDashboardComponent } from './responsable-dashboard/responsable-dashboard.component';
import { MaterialModule } from '../../material.module';
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';
import { ArchivesComponent } from 'app/components/archives/archives.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ResponsableLayoutComponent,
    ResponsableDashboardComponent,
    ArchivesComponent,
    
  ],
  imports: [
    CommonModule,
    ResponsableLayoutRoutingModule,
    MaterialModule,
    SharedModule,
    RouterModule,
    FormsModule 
  ]
})
export class ResponsableLayoutModule {}
