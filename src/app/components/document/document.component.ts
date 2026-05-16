import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../../services/document.service';
import { TransmissionService } from '../../services/transmission.service';
import { Document } from './document-form/document-form.component';
import { AuthService } from 'app/services/auth.service';
@Component({
  selector: 'app-document',
  templateUrl: './document.component.html'
})
export class DocumentComponent implements OnInit {

  showDetailsModal = false;
  documents: Document[] = [];
  currentDocument: Document = {} as Document;
  // Type courrier entrant/sortant
  type: 'entrant' | 'sortant' = 'entrant';
  role: string = '';
  // Formulaires
  showDocumentForm = false;
  selectedDocument: Document | null = null;
  showSendModal = false;
  // Employés
  employes: any[] = [];
  // Transmissions
  transmissionsRecues: any[] = [];
  transmissionsEnvoyees: any[] = [];
  tab: 'documents' | 'recus' | 'envoyes' = 'documents';
  selectedTransmission: any = null;
  selectedDoc: any = null;

  constructor(
    private documentService: DocumentService,
    private authService: AuthService,
    private transmissionService: TransmissionService,
  ) {}
  ngOnInit(): void {
    this.role = this.authService.getRole() || '';
    console.log("ROLE CONNECTÉ :", this.role);
    // Initialisation (CORRIGÉ)
    this.selectedTransmission = null;
    this.documents = [];
  
    this.transmissionsRecues = [];
    this.transmissionsEnvoyees = [];
    
    this.loadDocuments();
    this.loadEmployes();
    this.loadTransmissions();
   
  }
  loadEmployes() {
    this.documentService.getEmployes().subscribe({
      next: res => this.employes = res.data,
      error: err => console.error(err)
    });
  }
  loadDocuments(): void {
    if (this.role === 'RESPONSABLE') {
      if (this.type === 'entrant') {
        this.documentService.getMesCourriersEntrants().subscribe(res => {
          this.documents = res.data || [];
        });
      } else {
        this.documentService.getMesCourriersSortants().subscribe(res => {
          this.documents = res.data || [];
        });
      }
    }
  }
  loadTransmissions(): void {
    this.transmissionService.getRecus().subscribe({
      next: res => this.transmissionsRecues = res.data || [],
      error: err => console.error(err)
    });

    this.transmissionService.getEnvoyes().subscribe({
      next: res => this.transmissionsEnvoyees = res.data || [],
      error: err => console.error(err)
    });
  }
  changeType(type: 'entrant' | 'sortant') {
    this.type = type;
    this.loadDocuments();
  }

  changeTab(tab: 'documents' | 'recus' | 'envoyes') {
    this.tab = tab;
  }
  openDocumentForm(doc?: Document) {
  this.closeAllModals();

  this.currentDocument = doc ? { ...doc } : {} as Document;
  this.showDocumentForm = true;

  document.body.classList.add('modal-open');
}

  closeDocumentForm() {
    this.showDocumentForm = false;
    document.body.classList.remove('modal-open');
  }

  onDocumentSaved(savedDoc: Document) {
    this.loadDocuments();
    this.closeDocumentForm();
  }

  editDocument(doc: Document) {
  console.log("ÉTAPE 2 - Parent reçoit l'événement Modifier");
  this.openDocumentForm(doc);
}

  deleteDocument(id: number) {
    if (!confirm('Confirmer suppression ?')) return;

    const obs = this.type === 'entrant'
      ? this.documentService.deleteEntrant(id)
      : this.documentService.deleteSortant(id);

    obs.subscribe({
      next: () => this.loadDocuments(),
      error: () => alert('Erreur suppression document')
    });
  }
  openSendModal(doc: Document) {
  this.closeAllModals();

  this.selectedDocument = doc;
  this.showSendModal = true;

  document.body.classList.add('modal-open');
}

  closeSendModal() {
    this.showSendModal = false;
    this.selectedDocument = null;
    document.body.classList.remove('modal-open');
  }
  envoyerCourrier(event: any) {
    if (!this.selectedDocument) return;

    this.transmissionService.envoyer({
      courrierId: this.selectedDocument.id!,
      type: this.type.toUpperCase(),
      destinataireId: Number(event.employeId),
      message: event.message,
      dateEcheance: event.dateEcheance
    }).subscribe({
      next: () => {
        alert("Courrier envoyé !");
        this.closeSendModal();
        this.loadTransmissions();
      },
      error: () => alert("Erreur backend")
    });
  }
onView(doc: any) {
  this.closeAllModals();

  this.selectedDoc = doc;
  this.showDetailsModal = true;

  document.body.classList.add('modal-open');
}
  
closeViewModal() {
  this.selectedDoc = null;
  this.showDetailsModal = false;
  document.body.classList.remove('modal-open');
}

  viewDocument(doc: any) {
    console.log("CLICK VIEW =", doc);
    this.onView(doc);
  }
  
  get showBackdrop(): boolean {
    return this.selectedDoc || this.showDocumentForm || this.showSendModal;
  }

 private closeAllModals() {
  this.showDocumentForm = false;
  this.showSendModal = false;
  this.showDetailsModal = false;

  this.currentDocument = {} as Document;
  this.selectedDocument = null;
  this.selectedDoc = null;

  document.body.classList.remove('modal-open');
}
onTransmissionEnvoyee() {
  this.closeSendModal();
  this.loadTransmissions();
  alert('Courrier envoyé avec succès !');
}

}