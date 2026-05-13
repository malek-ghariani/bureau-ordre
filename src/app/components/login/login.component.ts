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

    // ✔ Vérification simple
    if (!this.email || !this.password) {
      this.errorMessage = "Veuillez remplir tous les champs";
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const credentials = {
      email: this.email,
      password: this.password
    };

    this.authService.login(credentials).subscribe({
      next: (response) => {

        console.log('Connexion réussie:', response);

        if (response?.token && response?.role) {

          // 🔐 Stockage session
          this.authService.saveToken(response.token);
          this.authService.saveRole(response.role);
          this.authService.saveEmail(response.email);
          this.authService.saveNom(response.nom);

          // 👉 IMPORTANT (si backend envoie id)
          if (response.id) {
            localStorage.setItem('id', response.id.toString());
          }

          // 🚀 REDIRECTION
          switch (response.role) {

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

        console.error(error);

        if (error.status === 401) {
          this.errorMessage = "Email ou mot de passe incorrect";
        } 
        else if (error.status === 0) {
          this.errorMessage = "Serveur indisponible";
        } 
        else {
          this.errorMessage = "Erreur inattendue";
        }

        this.isLoading = false;
      }
    });
  }
}