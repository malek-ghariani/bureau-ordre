import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Input() pageTitle: string = '';

  constructor(private router: Router) {}

  logout() {
    // Ici tu peux appeler ton service d'auth pour logout
    console.log('Déconnexion...');
    this.router.navigate(['/login']);
  }
}
