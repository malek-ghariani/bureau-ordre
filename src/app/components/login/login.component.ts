import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin() {
    if (!this.email || !this.password) {
      this.errorMessage = "Veuillez remplir tous les champs";
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (response) => {
        // ← response est ApiResponse<LoginResponse>
        // donc les données sont dans response.data
        const data = response.data;

        if (data?.token && data?.role) {
          this.authService.saveToken(data.token);
          this.authService.saveRole(data.role);
          this.authService.saveEmail(data.email);
          this.authService.saveNom(data.nom);
          this.authService.saveMatricule(data.matricule);
          if (data.id) this.authService.saveId(data.id);

          // Redirection selon le rôle
          switch (data.role) {
            case 'ADMIN':
              this.router.navigate(['/admin/employes']);
              break;
            case 'RESPONSABLE':
              this.router.navigate(['/responsable/documents']);
              break;
            case 'AGENT':
              this.router.navigate(['/agent/planifications']);
              break;
            default:
              this.router.navigate(['/login']);
          }
        } else {
          this.errorMessage = "Réponse invalide du serveur";
        }
        this.isLoading = false;
      },

      error: (error) => {
        if (error.status === 401) {
          this.errorMessage = "Email ou mot de passe incorrect";
        } else if (error.status === 0) {
          this.errorMessage = "Serveur indisponible";
        } else {
          this.errorMessage = "Erreur inattendue";
        }
        this.isLoading = false;
      }
    });
  }
}