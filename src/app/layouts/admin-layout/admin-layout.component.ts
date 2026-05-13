import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html'
})
export class AdminLayoutComponent {

menuItems = [
  { path: '/admin/departements', title: 'Gestion départements' },
  { path: '/employes', title: 'Employés', icon: 'people', class: '' },
  { path: '/user-profile', title: 'Profil', icon: 'person', class: '' },
];
pageTitle = '';

}
