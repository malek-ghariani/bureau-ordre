import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-default-redirect',
  template: `<p>Redirection...</p>`
})
export class DefaultRedirectComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    const role = this.authService.getRole();
    console.log("Rôle détecté :", role); // debug utile 👌

    if (role === 'ADMIN') {
      this.router.navigate(['/admin/dashboard']);
      return;
    }

    if (role === 'RESPONSABLE') {
      this.router.navigate(['/responsable/dashboard']);
      return;
    }

    if (role === 'AGENT') {
      this.router.navigate(['/agent/dashboard']);
      return;
    }

    // Si rôle inconnu
    this.router.navigate(['/unauthorized']);
  }
}
