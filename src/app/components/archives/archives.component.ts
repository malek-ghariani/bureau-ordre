import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../../services/document.service';

@Component({
  selector: 'app-archives',
  templateUrl: './archives.component.html'
})
export class ArchivesComponent implements OnInit {

  archivesEntrant: any[] = [];
  archivesSortant: any[] = [];
  tab: 'entrant' | 'sortant' = 'entrant';

  selectedArchive: any = null;
  showDetailsModal = false;

  // Filtres
  filtreReference = '';
  filtreExpediteur = '';
  filtreDateDebut = '';
  filtreDateFin = '';

  constructor(private documentService: DocumentService) {}

  ngOnInit(): void {
    this.loadArchivesEntrant();
    this.loadArchivesSortant();
  }

  loadArchivesEntrant() {
    this.documentService.getArchivesEntrant().subscribe({
      next: res => this.archivesEntrant = res.data || [],
      error: err => console.error(err)
    });
  }

  loadArchivesSortant() {
    this.documentService.getArchivesSortant().subscribe({
      next: res => this.archivesSortant = res.data || [],
      error: err => console.error(err)
    });
  }

  changeTab(tab: 'entrant' | 'sortant') {
    this.tab = tab;
    this.resetFiltres();
  }

  ouvrirDetails(archive: any) {
    this.selectedArchive = archive;
    this.showDetailsModal = true;

    console.log('Transmissions reçues :', JSON.stringify(this.selectedArchive.transmissions, null, 2));
  }

  fermerDetails() {
    this.selectedArchive = null;
    this.showDetailsModal = false;
  }

  resetFiltres() {
    this.filtreReference = '';
    this.filtreExpediteur = '';
    this.filtreDateDebut = '';
    this.filtreDateFin = '';
  }

  get archivesFiltrees(): any[] {
    const list = this.tab === 'entrant' ? this.archivesEntrant : this.archivesSortant;
    return list.filter(a => {
      const matchRef = !this.filtreReference ||
        a.reference?.toLowerCase().includes(this.filtreReference.toLowerCase());
      const matchExp = !this.filtreExpediteur ||
        a.expediteur?.toLowerCase().includes(this.filtreExpediteur.toLowerCase()) ||
        a.destinataire?.toLowerCase().includes(this.filtreExpediteur.toLowerCase());
      const matchDebut = !this.filtreDateDebut ||
        new Date(a.dateReception || a.dateEmission) >= new Date(this.filtreDateDebut);
      const matchFin = !this.filtreDateFin ||
        new Date(a.dateReception || a.dateEmission) <= new Date(this.filtreDateFin);
      return matchRef && matchExp && matchDebut && matchFin;
    });
  }

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
      error: err => console.error('Erreur téléchargement', err)
    });
  }
}