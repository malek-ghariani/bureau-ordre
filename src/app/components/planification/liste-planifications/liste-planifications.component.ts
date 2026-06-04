import { Component, Input, OnInit } from '@angular/core';
import { PlanificationService } from '../../../services/planification.service';


@Component({
  selector: 'app-liste-planifications',
  templateUrl: './liste-planifications.component.html',
  styleUrls: ['./liste-planifications.component.css']
})
export class ListePlanificationsComponent implements OnInit {

  planifications: any[] = []; 
  employes: any[] = [];

  showModal = false;
  selectedPlanification: any = null;

  showForm = false;
  isEdit = false;
  form: any = {};

  selectedReponse: { resultat: string; commentaireResultat: string } | null = null;

  constructor(
    private service: PlanificationService,
    
  ) {}

  ngOnInit(): void {
    this.loadPlanifications();
    
  }

  // ================= LISTE =================
 loadPlanifications() {
  this.service.getAll().subscribe({
    next: (res) => {
      this.planifications = res ?? []; // ← res.data pas res
    },
    error: (err) => console.error(err)
  });
}

  

 

  // ================= EDIT =================
  edit(p: any) {
    this.isEdit = true;
    this.form = { ...p };
    this.showForm = true;
  }

  // ================= DELETE =================
  delete(id: number) {
    if (confirm('Supprimer cette planification ?')) {
      this.service.delete(id).subscribe({
        next: () => this.loadPlanifications(),
        error: (err) => console.error(err)
      });
    }
  }

  // ================= VIEW MODAL =================
  view(p: any) {
    this.selectedPlanification = p;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.selectedPlanification = null;
  }

  // ================= FORM =================
 closeForm() {
  this.showForm = false;
}

savePlanification(data: any) {
  if (!this.isEdit) return; // pas de création manuelle

  this.service.update(data.id, data).subscribe({
    next: () => {
      this.loadPlanifications();
      this.closeForm();
    },
    error: (err: any) => console.error(err)
  });
}
  // ================= MODAL REPONSE =================
  showReponseModal = false;

 showReponse(p: any) {
  this.selectedReponse = {
    resultat: p.resultat,
    commentaireResultat: p.commentaireResultat  // ✅
  };
  this.showReponseModal = true;
}
}