import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgentLayoutComponent } from './agent-layout.component';
import { AgentDashboardComponent } from './agent-dashboard/agent-dashboard.component';


import { PlanificationEmployeComponent } from 'app/components/planification/planification-employe/planification-employe.component';

const routes: Routes = [
  {
    path: '',
    component: AgentLayoutComponent,
    children: [
      { path: '', redirectTo: 'planifications', pathMatch: 'full' },
      { path: 'dashboard', component: AgentDashboardComponent },
      { path: 'planifications', component: PlanificationEmployeComponent },
      
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AgentLayoutRoutingModule {}
