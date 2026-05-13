import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const expectedRole = route.data['role'] as string;
    const userRole = this.authService.getRole();
    const isLoggedIn = this.authService.isLoggedIn();

    // 🔹 1️⃣ Vérifier si l'utilisateur est connecté
    if (!isLoggedIn) {
      console.log('🔒 Utilisateur non connecté. Redirection vers login.');
      this.router.navigate(['/login']);
      return false;
    }

    // 🔹 2️⃣ Vérifier si la route attend un rôle spécifique
    if (expectedRole) {
      if (!userRole) {
        console.log('⚠️ Aucun rôle trouvé pour l’utilisateur connecté.');
        this.router.navigate(['/unauthorized']);
        return false;
      }

      if (userRole !== expectedRole) {
        console.log(`⚠️ Rôle incorrect. Rôle attendu : ${expectedRole}, rôle réel : ${userRole}`);
        this.router.navigate(['/unauthorized']);
        return false;
      }
    }

    // 🔹 3️⃣ Tout est OK, l'utilisateur peut accéder à la route
    return true;
  }
}
