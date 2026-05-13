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

  formReponse = {
    resultat: '',
     message: ''
  };

  resultatsOptions = [
    { value: "TERMINE", label: "Terminé" },
    { value: "TERMINE_ET_PLANIFIER", label: "Terminé et planifier le prochain" },
    { value: "MARQUE_COMME_LU", label: "Marqué comme lu" },
    { value: "A_REFAIRE", label: "À refaire" }
  ];

  constructor(
    private service: PlanificationService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.auth.getId();
    this.load(id);
    console.log("COMPONENT CHARGÉ");
  }

  load(id: number) {
    this.service.getByDestinataire(id).subscribe({
      next: (data) => {
        this.planifications = data;
      },
      error: (err) => console.error(err)
    });
  }

  // ================= VIEW =================
  ouvrirDetails(p: any) {
    this.selectedPlanification = p;
    this.showDetailsModal = true;
    document.body.classList.remove('modal-open');
  }

  fermerDetails() {
    this.showDetailsModal = false;
    this.selectedPlanification = null;
    document.body.classList.remove('modal-open');
  }

  // ================= REPONSE =================
  repondre(p: any) {
    this.selectedPlanification = p;
    this.showReponseModal = true;
    document.body.classList.remove('modal-open');
  }

  fermerReponse() {
    this.showReponseModal = false;
    this.formReponse = { resultat: '',  message:  '' };
    this.selectedPlanification = null;
    document.body.classList.remove('modal-open');
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
 

}