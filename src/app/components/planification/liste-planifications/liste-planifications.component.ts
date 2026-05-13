import { Component, Input, OnInit } from '@angular/core';
import { PlanificationService } from '../../../services/planification.service';
import { TransmissionService } from '../../../services/transmission.service';
import { EmployeDTO, EmployeService } from '../../../services/employe.service';

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

  selectedReponse: { resultat: string; message: string } | null = null;

  constructor(
    private service: PlanificationService,
    private transmissionService: TransmissionService,
    private employeService: EmployeService
  ) {}

  ngOnInit(): void {
    this.loadPlanifications();
    this.loadEmployes();
  }

  // ================= LISTE =================
  loadPlanifications() {
    this.service.getAll().subscribe({
      next: (res) => {
        this.planifications = res ?? [];
      },
      error: (err) => console.error(err)
    });
  }

  // ================= EMPLOYÉS =================
  loadEmployes() {
    this.employeService.getAll().subscribe({
      next: (res) => {
        this.employes = res.data ?? [];
      },
      error: (err) => console.error(err)
    });
  }

  // ================= CREATE =================
  create() {
    this.isEdit = false;
    this.form = {};
    this.showForm = true;
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

    const obs = this.isEdit
      ? this.service.update(data.id, data)
      : this.service.create(data);

    obs.subscribe({
      next: () => {
        this.loadPlanifications();
        this.closeForm();
      },
      error: (err) => console.error(err)
    });
  }

  // ================= MODAL REPONSE =================
  showReponseModal = false;

  showReponse(p: any) {

    this.selectedReponse = {
      resultat: p.resultat,
      message: p.message
    };

    this.showReponseModal = true;
  }
}