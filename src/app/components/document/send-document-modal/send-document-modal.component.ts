import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { Document } from '../document-form/document-form.component';
import { TransmissionService } from '../../../services/transmission.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-send-courrier',
  templateUrl: './send-document-modal.component.html'
})
export class SendDocumentModalComponent implements OnInit, OnChanges {
  @Input() showSendModal = false;
  @Input() document: Document | null = null;
  @Input() employes: any[] = [];
  @Input() type: 'entrant' | 'sortant' = 'entrant'; 

  @Output() close = new EventEmitter<void>();
  @Output() sent = new EventEmitter<void>(); // ✅ notifie le parent que c'est envoyé

  selectedEmployeId!: number;
  
  courrierMessage = '';
  dateEcheance: string = '';
  loading = false;
  errorMessage = '';

  constructor(
    private transmissionService: TransmissionService
  ) {}

  ngOnInit() {}

  ngOnChanges() {}

  closeSendModal() {
    this.close.emit();
    this.resetForm();
  }

  envoyerCourrier() {
    if (!this.selectedEmployeId  || !this.courrierMessage || !this.dateEcheance)
      return;

    this.loading = true;
    this.errorMessage = '';

    const payload = {
      destinataireId: this.selectedEmployeId,
      
      message: this.courrierMessage,          // ✅ message du responsable → TransmissionCourrier.message
      dateEcheance: this.dateEcheance,
      courrierId: this.document?.id ?? null,
      type: this.type.toUpperCase()
    };

    this.transmissionService.envoyer(payload).subscribe({
      next: () => {
        this.loading = false;
        this.sent.emit();       // ✅ notifie le parent
        this.closeSendModal();
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = 'Erreur lors de l\'envoi. Veuillez réessayer.';
        console.error(err);
      }
    });
  }

  private resetForm() {
    this.selectedEmployeId = undefined!;
   
    this.courrierMessage = '';
    this.dateEcheance = '';
    this.errorMessage = '';
  }
}