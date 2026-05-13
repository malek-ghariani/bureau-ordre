import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';
import { AuthGuard } from './services/auth.guard';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { DefaultRedirectComponent } from './components/default-redirect/default-redirect.component';
import { ListePlanificationsComponent } from './components/planification/liste-planifications/liste-planifications.component';

const routes: Routes = [
  // 🔹 Page Login (publique)
  { path: 'login', component: LoginComponent },

  // 🔹 Page "Non autorisé"
  { path: 'unauthorized', component: UnauthorizedComponent },

  // 🔹 Page User Profile (accessible à tous les utilisateurs connectés)
  { path: 'user-profile', component: UserProfileComponent, canActivate: [AuthGuard] },

  // 🔹 Default redirect après login selon rôle
  { path: 'default', component: DefaultRedirectComponent, canActivate: [AuthGuard] },

  // 🔹 Module Admin (lazy loading, rôle ADMIN)
  {
    path: 'admin',
    loadChildren: () =>
      import('./layouts/admin-layout/admin-layout.module').then(m => m.AdminLayoutModule),
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },

  // 🔹 Module Responsable (lazy loading, rôle RESPONSABLE)
  {
    path: 'responsable',
    loadChildren: () =>
      import('./layouts/responsable-layout/responsable-layout.module').then(m => m.ResponsableLayoutModule),
    canActivate: [AuthGuard],
    data: { role: 'RESPONSABLE' }
  },

  // 🔹 Module Agent (lazy loading, rôle AGENT)
  {
    path: 'agent',
    loadChildren: () =>
      import('./layouts/agent-layout/agent-layout.module').then(m => m.AgentLayoutModule),
    canActivate: [AuthGuard],
    data: { role: 'AGENT' }
  },

  // 🔹 Redirection par défaut vers /login
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // 🔹 Toutes les routes inconnues redirigent vers Login
  { path: '**', redirectTo: 'login' },
  {
  path: 'planifications',
  component: ListePlanificationsComponent
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
