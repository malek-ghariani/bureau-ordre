// src/app/admin/employe-list/employe-list.component.ts
import { Component, OnInit } from '@angular/core';
import { EmployeService, EmployeDTO } from '../../services/employe.service';
import { DepartementService } from '../../services/departement.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-employe-list',
  templateUrl: './employe-list.component.html',
  styleUrls: ['./employe-list.component.css']
})
export class EmployeListComponent implements OnInit {
  employes: EmployeDTO[] = [];
  departements: { code: string, nom: string }[] = [];
  selectedDepartement: string = '';
  isAdmin: boolean = false;

  constructor(
    private employeService: EmployeService,
    private departementService: DepartementService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole('ADMIN');
    if (!this.isAdmin) return;

    this.loadDepartements();
    this.loadEmployes();
  }

  loadEmployes(): void {
    if (this.selectedDepartement) {
      this.employeService.getByDepartement(this.selectedDepartement).subscribe(res => {
        if (res.success) this.employes = res.data || [];
      });
    } else {
      this.employeService.getAll().subscribe(res => {
        if (res.success) this.employes = res.data || [];
      });
    }
  }

  loadDepartements(): void {
    this.departementService.getAll().subscribe(res => {
      if (res.success) this.departements = res.data || [];
    });
  }

  deleteEmploye(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer cet employé ?')) {
      this.employeService.delete(id).subscribe(res => {
        if (res.success) this.loadEmployes();
        else alert(res.message);
      });
    }
  }

  onDepartementChange(): void {
    this.loadEmployes();
  }
}
