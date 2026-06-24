import { Component, OnInit } from '@angular/core';
import { PlanificationService } from 'app/services/planification.service';
import { AuthService } from 'app/services/auth.service';
import { DocumentService } from 'app/services/document.service';

@Component({
  selector: 'app-planification-employe',
  templateUrl: './planification-employe.component.html'
})
export class PlanificationEmployeComponent implements OnInit {

  planifications: any[] = [];
  showReponseModal = false;
  showDetailsModal = false;
  selectedPlanification: any = null;

  formReponse = { resultat: '', commentaireResultat: '' };

  resultatsOptions = [
    { value: 'TERMINE',               label: 'Terminé' },
    { value: 'TERMINE_ET_PLANIFIER',  label: 'Terminé et planifier le prochain' },
    { value: 'MARQUE_COMME_LU',       label: 'Marqué comme lu' },
    { value: 'A_REFAIRE',             label: 'À refaire' }
  ];

  constructor(
    private service: PlanificationService,
    private auth: AuthService,
    private documentService: DocumentService
  ) {}

  ngOnInit(): void {
     this.load();
  }

  load() {
  this.service.getMesPlanifications().subscribe({
    next: (res) => this.planifications = res ?? [],
    error: (err: any) => console.error(err)
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
    this.formReponse = { resultat: '', commentaireResultat: '' };
    this.selectedPlanification = null;
  }

  envoyerReponse() {
  if (!this.selectedPlanification?.id) return;

  this.service.repondre(this.selectedPlanification.id, {
    commentaireResultat: this.formReponse.commentaireResultat,
    resultat: this.formReponse.resultat
  }).subscribe({
    next: () => {
      this.fermerReponse();
      this.load();
    },
    error: (err: any) => console.error(err)
  });
}

  // ================= PIÈCES JOINTES =================
telechargerPieceJointe(id: number, nom: string) {
  this.documentService.downloadPieceJointe(id).subscribe({
    next: (blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = nom;
      a.click();
      window.URL.revokeObjectURL(url);
    },
    error: (err: any) => console.error('Erreur téléchargement', err)
  });
}
  ouvrirReponseDepuisDetails() {
  const p = this.selectedPlanification;
  this.showDetailsModal = false;
  // ne pas null selectedPlanification ici
  this.showReponseModal = true;
}
filtreStatut = 'EN_ATTENTE'; // par défaut

get planificationsFiltrees() {
  if (this.filtreStatut === 'TOUS') return this.planifications;
  return this.planifications.filter(p => p.statut === this.filtreStatut);
}

isEcheanceDepassee(dateEcheance: string): boolean {
  if (!dateEcheance) return false;
  return new Date(dateEcheance) < new Date();
}
}