import { Component, OnInit } from '@angular/core';
import { DepartementService, DepartementDTO } from 'app/services/departement.service';

@Component({
  selector: 'app-departement',
  templateUrl: './departement.component.html',
  styleUrls: ['./departement.component.css']
})
export class DepartementComponent implements OnInit {
  departements: DepartementDTO[] = [];
  selected: DepartementDTO = {} as DepartementDTO;
  showForm = false;
  isEditing = false;

  constructor(private departementService: DepartementService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.departementService.getAll().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.departements = res.data;
        }
      },
      error: (err) => {
        console.error('Erreur chargement départements:', err);
        alert('Erreur de chargement des départements');
      }
    });
  }

  add() {
    this.selected = {} as DepartementDTO;
    this.isEditing = false;
    this.showForm = true;
  }

  edit(dept: DepartementDTO) {
    this.selected = { ...dept };
    this.isEditing = true;
    this.showForm = true;
  }

  save(event?: Event) {
    if (event) {
      event.preventDefault();
    }

    console.log('Données à sauvegarder:', this.selected);

    // Validation
    if (!this.selected.code || !this.selected.nom) {
      alert('Le code et le nom sont obligatoires');
      return;
    }

    if (this.isEditing) {
      // Modification
      this.departementService.update(this.selected.code, this.selected).subscribe({
        next: (res) => {
          console.log('✅ Modification réussie:', res);
          this.showForm = false;
          this.load();
        },
        error: (err) => {
          console.error('❌ Erreur modification:', err);
          alert(`Erreur: ${err.error?.message || err.message}`);
        }
      });
    } else {
      // Création
      this.departementService.create(this.selected).subscribe({
        next: (res) => {
          console.log('✅ Création réussie:', res);
          this.showForm = false;
          this.load();
        },
        error: (err) => {
          console.error('❌ Erreur création:', err);
          alert(`Erreur: ${err.error?.message || err.message}`);
        }
      });
    }
  }

  remove(code: string) {
    if (confirm(`Voulez-vous vraiment supprimer le département ${code} ?`)) {
      this.departementService.delete(code).subscribe({
        next: () => {
          console.log('✅ Suppression réussie');
          this.load();
        },
        error: (err) => {
          console.error('❌ Erreur suppression:', err);
          alert('Erreur lors de la suppression');
        }
      });
    }
  }

  cancel() {
    this.showForm = false;
    this.selected = {} as DepartementDTO;
  }
}