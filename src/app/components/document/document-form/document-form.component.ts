import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { DocumentService } from '../../../services/document.service';
import { HttpEventType } from '@angular/common/http';
import { PieceJointeDTO } from 'app/models/piece-jointe.model';
import { TiersService } from '../../../services/tiers.service';
import { Tiers } from 'app/models/tiers.model';
import { ApiResponse } from 'app/models/ApiResponse.model';

export interface Document {
  id?: number;
  numeroOrdre?: string;       // ← ajoute
  dateReception?: string;
  dateEmission?: string;
  dateSaisie?: string;        // ← ajoute
  typeDocument: string;
  reference: string;
  nature: string;
  expediteurId?: number;      // ← pour Spring
        
  nomExpediteur?: string;     // ← retourné par Spring
  destinataireId?: number;    // ← pour Spring
  
  nomDestinataire?: string;   // ← retourné par Spring
  modeReception?: string;
  modeExpedition?: string;
  priorite: string;
  statut: string;
  etat?: string;
}

@Component({
  selector: 'app-document-form',
  templateUrl: './document-form.component.html'
})
export class DocumentFormComponent implements OnChanges {
  
  @Input() showModal = false;
  @Input() currentDocument!: Document;
  @Input() type: 'entrant' | 'sortant' = 'entrant';

  @Output() closeForm = new EventEmitter<void>();
  @Output() savedDocument = new EventEmitter<Document>();

  tiers: any[] = []; 
  pieceJointes: any[] = [];
  selectedFiles: File[] = [];
  filePreviewUrl: string | null = null;
  fileDescription = '';
  fileUploadProgress = 0;
  isUploading = false;
  isEditMode = false;
  today = '';

  constructor(
    private documentService: DocumentService,
    private tiersService: TiersService
  ) {
    this.today = this.getTodayDate();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['showModal'] && this.showModal) {
      this.loadTiers();  // ← charge à l'ouverture
    }
    if (changes['currentDocument'] && this.currentDocument) {
      this.isEditMode = !!this.currentDocument.id;

      this.selectedFiles = [];
      this.filePreviewUrl = null;
      this.fileDescription = '';
      this.pieceJointes = [];

     if (this.isEditMode && this.currentDocument.id) {
      // mode édition → charger les pièces jointes, ne pas toucher aux dates
      this.loadPieceJointes(this.currentDocument.id);
    } else {
      // mode création → pré-remplir avec la date du jour
      this.currentDocument.dateReception = this.today;
      this.currentDocument.dateEmission = this.today;
    }
    }
  }
  

  private getTodayDate(): string {
    const d = new Date();
    return d.toISOString().split('T')[0];
  }

  formatDate(date: any): string {
    if (!date) return '';
    return new Date(date).toISOString().split('T')[0];
  }

  onFileSelected(event: any) {
  const files: FileList = event.target.files;
  if (!files || files.length === 0) return;

  this.selectedFiles = Array.from(files).filter(f => {
    if (f.size > 10 * 1024 * 1024) {
      alert(`Fichier ${f.name} trop volumineux (max 10MB)`);
      return false;
    }
    return true;
  });

  // Preview seulement pour le premier fichier image
  const first = this.selectedFiles[0];
  if (first?.type.startsWith('image/')) {
    const reader = new FileReader();
    reader.onload = e => this.filePreviewUrl = (e.target as any).result;
    reader.readAsDataURL(first);
  } else {
    this.filePreviewUrl = null;
  }
}
  loadTiers() {
    this.tiersService.getAll().subscribe({
      next: res => {
        if (res.success && res.data) {
          this.tiers = res.data;
        }
      },
      error: err => console.error('Erreur chargement tiers:', err)
    });
  }


  removeFile() {
    this.selectedFiles = [];
    this.filePreviewUrl = null;
    this.fileDescription = '';
  }

 saveDocument() {
  if (!this.currentDocument) return;
 console.log('currentDocument:', this.currentDocument);
  const data: any = { ...this.currentDocument };
console.log('data envoyé:', data); 
  if (data.dateReception) data.dateReception = this.formatDate(data.dateReception);
  if (data.dateEmission) data.dateEmission = this.formatDate(data.dateEmission);

  if (this.type === 'entrant') {
    data.modeReception = data.modeReception || 'EMAIL';
  } else {
    data.modeExpedition = data.modeExpedition || 'EMAIL';
  }

  
  data.priorite = data.priorite || 'NORMALE';
  data.etat = data.etat || 'ACTIVE';

  this.isUploading = true;

  const obs = this.isEditMode
    ? (this.type === 'entrant'
        ? this.documentService.updateEntrant(data.id!, data)
        : this.documentService.updateSortant(data.id!, data))
    : (this.type === 'entrant'
        ? this.documentService.createEntrant(data)
        : this.documentService.createSortant(data));

  obs.subscribe({
    next: res => {
      const docId = res.data.id;

     if (this.selectedFiles.length > 0) {
  this.uploadPieceJointe(docId);
} else {
  this.finishSave(res.data);
}
    },
    error: err => {
      this.isUploading = false;
      console.error(err);
      alert('Erreur sauvegarde document');
    }
  });
}

 private uploadPieceJointe(docId: number) {

  if (!this.selectedFiles || this.selectedFiles.length === 0) {
    this.finishSave(this.currentDocument);
    return;
  }

  const upload = this.type === 'entrant'
        ? this.documentService.uploadPieceJointeEntrant(docId, this.selectedFiles, this.fileDescription)
        : this.documentService.uploadPieceJointeSortant(docId, this.selectedFiles, this.fileDescription);

    upload.subscribe({
        next: () => {
            this.loadPieceJointes(docId);
            this.resetFile();
            this.finishSave(this.currentDocument);
        },
        error: err => {
            console.error(err);
            alert("Erreur upload pièces jointes");
            this.finishSave(this.currentDocument);
        }
    });
}
 private afterSave(doc: Document) {
  this.isUploading = false;        // Débloque le bouton
  this.fileUploadProgress = 0;
  this.savedDocument.emit(doc);
  this.close();
}

close() {
  
   this.showNewTiersForm = false;
  this.resetNewTiers();
  this.closeForm.emit();
  this.currentDocument = {} as Document;
  this.resetFile(); // ⭐ mieux que répéter
  this.pieceJointes = [];
}
private resetFile() {
  this.selectedFiles = [];
  this.filePreviewUrl = null;
  this.fileDescription = '';
  this.fileUploadProgress = 0;
}
private finishSave(doc: Document) {
  this.isUploading = false;
  this.savedDocument.emit(doc);
  this.close();
}

loadPieceJointes(docId: number) {
  const obs = this.type === 'entrant'
    ? this.documentService.getPieceJointesEntrant(docId)
    : this.documentService.getPieceJointesSortant(docId);

  obs.subscribe({
    next: pieces => {
      console.log('Pièces jointes:', pieces);
      this.pieceJointes = pieces;
    },
    error: err => {
      console.error('Erreur lors du chargement des pièces jointes', err);
      this.pieceJointes = [];
    }
  });
}

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ['Bytes', 'KB', 'MB', 'GB'][i];
  }

  getFileIcon(fileName: string): string {
    const ext = fileName.split('.').pop()?.toLowerCase();
    const icons: any = {
      pdf: 'fas fa-file-pdf text-danger',
      doc: 'fas fa-file-word text-primary',
      docx: 'fas fa-file-word text-primary',
      xls: 'fas fa-file-excel text-success',
      xlsx: 'fas fa-file-excel text-success',
      ppt: 'fas fa-file-powerpoint text-warning',
      pptx: 'fas fa-file-powerpoint text-warning',
      jpg: 'fas fa-file-image text-info',
      jpeg: 'fas fa-file-image text-info',
      png: 'fas fa-file-image text-info',
      gif: 'fas fa-file-image text-info',
      txt: 'fas fa-file-alt text-secondary'
    };
    return icons[ext!] || 'fas fa-file text-secondary';
  }
  deletePieceJointe(id: number) {
  if (!confirm("Supprimer cette pièce jointe ?")) return;

  this.documentService.deletePieceJointe(id).subscribe({
    next: () => {
      // Mise à jour locale
      this.pieceJointes = this.pieceJointes.filter(p => p.id !== id);
    },
    error: err => {
      console.error(err);
      alert("Erreur lors de la suppression.");
    }
  });
}

openPieceJointe(piece: PieceJointeDTO) {
  this.documentService.downloadPieceJointe(piece.id).subscribe(blob => {
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  }, err => {
    console.error("Erreur téléchargement fichier", err);
  });
}
onFilesSelected(event: any) {
  const files: FileList = event.target.files;
  this.selectedFiles = Array.from(files);
}
newTiers: Tiers = { nom: '', email: '', telephone: '', type: 'PERSONNE_PHYSIQUE' };
showNewTiersForm = false;
toggleNewTiersForm() {
  this.showNewTiersForm = !this.showNewTiersForm;
  if (!this.showNewTiersForm) {
    this.resetNewTiers();
  }
}

private resetNewTiers() {
  this.newTiers = { nom: '', email: '', telephone: '', type: 'PERSONNE_PHYSIQUE' };
}
saveTiers() {
  if (!this.newTiers.nom?.trim()) return;

  this.tiersService.create(this.newTiers).subscribe({
    next: (res: ApiResponse<Tiers>) => {
      if (res.success && res.data) {
        this.tiers.push(res.data);
        if (this.type === 'entrant') {
          this.currentDocument.expediteurId = res.data.id;
        } else {
          this.currentDocument.destinataireId = res.data.id;
        }
        this.showNewTiersForm = false;
        this.resetNewTiers();
      }
    },
    error: err => {
      console.error('Erreur création tiers:', err);
      alert('Erreur lors de la création du tiers');
    }
  });
}
  
}
