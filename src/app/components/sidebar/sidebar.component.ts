import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  menuItems: any[] = [];
  role = localStorage.getItem('role');

  constructor(private router: Router) {}

  ngOnInit() {

    console.log('ROLE =', this.role);

    if (this.role === 'RESPONSABLE') {
      this.menuItems = [
        { path: '/responsable/documents', title: 'Courriers' },
        { path: '/responsable/planifications', title: 'Planification' }
      ];
    }

    else if (this.role === 'ADMIN') {
      this.menuItems = [
        { path: '/admin/employes', title: 'Gestion employés' },
        { path: '/admin/departement', title: 'Gestion départements' }
      ];
    }
  }

  navigate(path: string) {
    console.log('NAVIGATE TO:', path);
    this.router.navigateByUrl(path);
  }
}