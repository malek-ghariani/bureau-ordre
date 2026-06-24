import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Document } from '../document-form/document-form.component';

@Component({
  selector: 'app-document-list',
  templateUrl: './document-list.component.html'
})
export class DocumentListComponent {

  /** Liste des documents affichés */
  @Input() documents: Document[] = [];

  /** Type actuel (entrant / sortant) */
  @Input() type: 'entrant' | 'sortant' = 'entrant';

  /** Rôle de l'utilisateur (RESPONSABLE / EMPLOYE) */
  @Input() role: string = '';

  /** Événements envoyés au parent */
  @Output() changeTypeEvent = new EventEmitter<'entrant' | 'sortant'>();
  @Output() createDocumentEvent = new EventEmitter<void>();
  @Output() editDocumentEvent = new EventEmitter<Document>();
  @Output() deleteDocumentEvent = new EventEmitter<number>();
  @Output() sendDocumentEvent = new EventEmitter<Document>();
  @Output() viewDocumentEvent = new EventEmitter<Document>();
  @Output() archiverDocumentEvent = new EventEmitter<Document>();
  @Output() marquerTraiteEvent = new EventEmitter<Document>()

  constructor() {}

  changeType(type: 'entrant' | 'sortant') {
    this.changeTypeEvent.emit(type);
  }

  createDocument() {
    this.createDocumentEvent.emit();
  }

 editDocument(doc: Document) {
  
  this.editDocumentEvent.emit(doc);
}

  deleteDocument(id: number) {
    this.deleteDocumentEvent.emit(id);
  }

  openSendModal(doc: Document) {
    this.sendDocumentEvent.emit(doc);
  }

  viewDocument(doc: Document) {
    
  this.viewDocumentEvent.emit(doc);
}
archiver(doc: Document) {
  this.archiverDocumentEvent.emit(doc);
}
marquerTraite(doc: Document) {
  this.marquerTraiteEvent.emit(doc);
}
// Filtres
filtreReference = '';
filtreDateDebut = '';
filtreDateFin   = '';

get documentsFiltres() {
  return this.documents.filter(doc => {
    // Filtre référence
    if (this.filtreReference &&
        !doc.reference?.toLowerCase().includes(this.filtreReference.toLowerCase())) {
      return false;
    }

    // La date à comparer selon le type
    const dateStr = this.type === 'entrant' ? doc.dateReception : doc.dateEmission;
    const date = dateStr ? new Date(dateStr) : null;

    if (this.filtreDateDebut && date) {
      if (date < new Date(this.filtreDateDebut)) return false;
    }
    if (this.filtreDateFin && date) {
      if (date > new Date(this.filtreDateFin)) return false;
    }

    return true;
  });
}

resetFiltres() {
  this.filtreReference = '';
  this.filtreDateDebut = '';
  this.filtreDateFin   = '';
}
}