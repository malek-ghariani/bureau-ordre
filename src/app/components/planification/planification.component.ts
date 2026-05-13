import { Component, OnInit } from '@angular/core';
import { PlanificationService } from '../../services/planification.service';
import { AuthService } from 'app/services/auth.service';

@Component({
  selector: 'app-planification',
  templateUrl: './planification.component.html',
  styleUrls: ['./planification.component.css']
})
export class PlanificationComponent implements OnInit {

  role: string = '';

  planifications: any[] = [];
  planificationsEmploye: any[] = [];

  showForm: boolean = false;
  selected: any = null;

  idEmploye!: number;

  constructor(
    private planificationService: PlanificationService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.role = this.auth.getRole()!;

    if (this.role === 'RESPONSABLE') {
      this.loadAllPlanifications();
    } else {
      this.idEmploye = Number(localStorage.getItem('id'));
      this.loadMyPlanifications();
    }
  }

  // RESPONSABLE
  loadAllPlanifications(): void {
    this.planificationService.getAll().subscribe({
      next: (res) => {
        this.planifications = res;
      },
      error: (err) => console.error(err)
    });
  }

  // EMPLOYÉ
  loadMyPlanifications(): void {

    const id = Number(localStorage.getItem('id'));

    this.planificationService.getByDestinataire(id).subscribe({
      next: (data) => {
        console.log("DATA PLANIFICATIONS =", data);
        this.planificationsEmploye = data;
      },
      error: (err) => console.error(err)
    });
  }
}