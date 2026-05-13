import { Component } from '@angular/core';

@Component({
  selector: 'app-responsable-layout',
  templateUrl: './responsable-layout.component.html',
  styleUrls: ['./responsable-layout.component.css']
})
export class ResponsableLayoutComponent { 
  menuItems = [
  { path: '/dashboard', title: 'Dashboard', icon: 'dashboard', class: '' },
  { path: '/courriers', title: 'Courriers' },
  { path: '/planification', title: 'Planification' }
];
pageTitle = '';

}
