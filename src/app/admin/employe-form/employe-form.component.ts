// src/app/admin/employe-form/employe-form.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmployeService, EmployeDTO } from '../../services/employe.service';
import { DepartementService } from '../../services/departement.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-employe-form',
  templateUrl: './employe-form.component.html',
  styleUrls: ['./employe-form.component.css']
})
export class EmployeFormComponent implements OnInit {
  form: FormGroup;
  departements: { code: string, nom: string }[] = [];
  id?: number;

  roles = ['ADMIN', 'RESPONSABLE_BUREAU', 'AGENT'];
  postes = ['POSTE1', 'POSTE2']; // adapte selon ton enum PosteEmploye

  constructor(
    private fb: FormBuilder,
    private employeService: EmployeService,
    private departementService: DepartementService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      matricule: ['', Validators.required],
      nom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      telephone: [''],
      poste: [''],
      departementCode: [''],
      role: ['AGENT'],
      enabled: [true]
    });
  }

  ngOnInit(): void {
    this.loadDepartements();
    this.id = this.route.snapshot.params['id'];

    if (this.id) {
      this.employeService.getById(this.id).subscribe(res => {
        if (res.success && res.data) {
          this.form.patchValue(res.data);
        }
      });
    }
  }

  loadDepartements(): void {
    this.departementService.getAll().subscribe(res => {
      if (res.success) this.departements = res.data || [];
    });
  }

  submit(): void {
    const employe: EmployeDTO = this.form.value;

    if (this.id) {
      this.employeService.update(this.id, employe).subscribe(res => {
        if (res.success) this.router.navigate(['/admin/employes']);
        else alert(res.message);
      });
    } else {
      this.employeService.create(employe).subscribe(res => {
        if (res.success) this.router.navigate(['/admin/employes']);
        else alert(res.message);
      });
    }
  }
}
