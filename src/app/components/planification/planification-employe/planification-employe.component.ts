import { Component, OnInit } from '@angular/core';
import { PlanificationService } from 'app/services/planification.service';
import { AuthService } from 'app/services/auth.service';

@Component({
  selector: 'app-planification-employe',
  templateUrl: './planification-employe.component.html'
})
export class PlanificationEmployeComponent implements OnInit {

  planifications: any[] = [];
  showReponseModal = false;
  showDetailsModal = false;
  selectedPlanification: any = null;

  formReponse = { resultat: '', message: '' };

  resultatsOptions = [
    { value: 'TERMINE',               label: 'Terminé' },
    { value: 'TERMINE_ET_PLANIFIER',  label: 'Terminé et planifier le prochain' },
    { value: 'MARQUE_COMME_LU',       label: 'Marqué comme lu' },
    { value: 'A_REFAIRE',             label: 'À refaire' }
  ];

  constructor(
    private service: PlanificationService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.load(this.auth.getId());
  }

  load(id: number) {
    this.service.getByDestinataire(id).subscribe({
      next: (data) => this.planifications = data,
      error: (err) => console.error(err)
    });
  }

  // ================= DÉTAILS =================
  ouvrirDetails(p: any) {
    this.selectedPlanification = p;
    this.showDetailsModal = true;
  }

  fermerDetails() {
    this.showDetailsModal = false;
    this.selectedPlanification = null;
  }

  // ================= RÉPONSE =================
  repondre(p: any) {
    this.selectedPlanification = p;
    this.showReponseModal = true;
  }

  fermerReponse() {
    this.showReponseModal = false;
    this.formReponse = { resultat: '', message: '' };
    this.selectedPlanification = null;
  }

  envoyerReponse() {
    if (!this.selectedPlanification?.id) return;

    this.service.repondre(
      this.selectedPlanification.id,
      this.formReponse.resultat,
      this.formReponse.message
    ).subscribe({
      next: () => {
        this.fermerReponse();
        this.load(this.auth.getId());
      },
      error: (err) => console.error(err)
    });
  }

  // ================= PIÈCES JOINTES =================
  telechargerPieceJointe(id: number, nom: string) {
    this.service.telechargerPieceJointe(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = nom;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => console.error('Erreur téléchargement', err)
    });
  }
}