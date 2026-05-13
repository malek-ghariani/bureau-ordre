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

  constructor() {}

  changeType(type: 'entrant' | 'sortant') {
    this.changeTypeEvent.emit(type);
  }

  createDocument() {
    this.createDocumentEvent.emit();
  }

 editDocument(doc: Document) {
  console.log("ÉTAPE 1 - Bouton Modifier cliqué dans document-list");
  this.editDocumentEvent.emit(doc);
}

  deleteDocument(id: number) {
    this.deleteDocumentEvent.emit(id);
  }

  openSendModal(doc: Document) {
    this.sendDocumentEvent.emit(doc);
  }

  viewDocument(doc: Document) {
    console.log("🔥 CLICK VIEW CHILD", doc);
  this.viewDocumentEvent.emit(doc);
}
}