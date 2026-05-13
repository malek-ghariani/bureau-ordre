import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Document } from '../document-form/document-form.component';

@Component({
  selector: 'app-send-courrier',
  templateUrl: './send-document-modal.component.html'
})
export class SendDocumentModalComponent {
  @Input() showSendModal = false;
  @Input() document: Document | null = null;
  @Input() employes: any[] = [];

  @Output() close = new EventEmitter<void>();
  @Output() send = new EventEmitter<{ employeId: number; objet: string; message: string; dateEcheance: string}>();

  selectedEmployeId!: number;
  courrierObjet = '';
  courrierMessage = '';
  dateEcheance: string = '';

    ngOnInit() {
    console.log('document dans modal', this.document);
    console.log('employes dans modal', this.employes);
  } 

  ngOnChanges() {
  console.log('document:', this.document);
  console.log('employes:', this.employes);
  console.log('showSendModal:', this.showSendModal);
}
  closeSendModal() {
    this.close.emit();
    this.selectedEmployeId = undefined!;
    this.courrierObjet = '';
    this.courrierMessage = '';
  }

 envoyerCourrier() {
  if (!this.selectedEmployeId || !this.courrierObjet || !this.courrierMessage || !this.dateEcheance)
    return;

  this.send.emit({
     employeId: this.selectedEmployeId,
    objet: this.courrierObjet,
    message: this.courrierMessage,
    dateEcheance: this.dateEcheance,
    
  });
}

}