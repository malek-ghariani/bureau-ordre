import { Component, OnInit } from '@angular/core';
import { EmployeService, EmployeDTO } from 'app/services/employe.service';

@Component({
  selector: 'app-employe',
  templateUrl: './employe.component.html',
  styleUrls: ['./employe.component.css']
})
export class EmployeComponent implements OnInit {
  employees: EmployeDTO[] = [];
  selected: EmployeDTO = {} as EmployeDTO;
  showForm = false;

  constructor(private employeeService: EmployeService) {}

  ngOnInit() {
    console.log('Token présent:', localStorage.getItem('token'));
    this.load();
  }

  load() {
    this.employeeService.getAll().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.employees = res.data;
        }
      },
      error: (err) => {
        console.error('Erreur chargement:', err);
      }
    });
  }

  add() {
    this.selected = {} as EmployeDTO;
    this.selected.role = 'AGENT';
    this.selected.departementCode = 'DEP001'; // CORRIGÉ: DEP001 au lieu de DEP01
    this.selected.enabled = true;
    this.showForm = true;
  }

  edit(emp: EmployeDTO) {
    this.selected = { ...emp };
    if (this.selected.enabled === undefined || this.selected.enabled === null) {
      this.selected.enabled = true;
    }
    this.showForm = true;
  }

  save(event?: Event) {
    // 🔴 EMPÊCHER LA SOUMISSION PAR DÉFAUT
    if (event) {
      event.preventDefault();
    }

    console.log('🟢 save() appelée');
    
    const payload: EmployeDTO = { ...this.selected };
    
    // 🔴 LOG POUR VOIR LES DONNÉES
    console.log('📤 Données à envoyer:', JSON.stringify(payload, null, 2));

    // Vérifications
    if (!payload.nom || !payload.email || !payload.matricule) {
      alert('Veuillez remplir tous les champs obligatoires');
      return;
    }

    // Gestion du mot de passe
    if (payload.id && !payload.password) {
      delete payload.password;
    }
    if (!payload.id) {
      payload.enabled = true;
    }

    if (payload.id) {
      this.employeeService.update(payload.id, payload).subscribe({
        next: (res) => {
          console.log('✅ Modification réussie:', res);
          this.showForm = false;
          this.load();
        },
        error: (err) => {
          console.error('❌ Erreur modification:', err);
          alert(`Erreur ${err.status}: ${err.error?.message || err.message}`);
        }
      });
    } else {
      this.employeeService.create(payload).subscribe({
        next: (res) => {
          console.log('✅ Création réussie:', res);
          this.showForm = false;
          this.load();
        },
        error: (err) => {
          console.error('❌ Erreur création:', err);
          alert(`Erreur ${err.status}: ${err.error?.message || err.message}`);
        }
      });
    }
  }

  remove(id: number) {
    if (confirm("Voulez-vous vraiment supprimer cet employé ?")) {
      this.employeeService.delete(id).subscribe({
        next: () => this.load(),
        error: (err) => {
          console.error('Erreur suppression:', err);
          alert('Erreur lors de la suppression');
        }
      });
    }
  }
}