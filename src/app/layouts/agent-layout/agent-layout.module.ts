import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgentLayoutComponent } from './agent-layout.component';
import { AgentLayoutRoutingModule } from './agent-layout.routing';
import { AgentDashboardComponent } from './agent-dashboard/agent-dashboard.component';
import { MaterialModule } from '../../material.module';
import { SharedModule } from '../../shared/shared.module';
import { TransmissionsRecuesComponent } from './transmissions/transmissions-recues/transmissions-recues.component';
import { TransmissionViewComponent } from './transmissions/transmission-view/transmission-view.component';

@NgModule({
  declarations: [
    AgentLayoutComponent,
    AgentDashboardComponent,
    TransmissionsRecuesComponent,
    TransmissionViewComponent,
    
    
  ],
  imports: [
    CommonModule,
    AgentLayoutRoutingModule,
    MaterialModule,
    SharedModule
  ]
})
export class AgentLayoutModule {}
