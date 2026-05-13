import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { DocumentService } from '../../../services/document.service';
import { HttpEventType } from '@angular/common/http';
import { PieceJointeDTO } from 'app/models/piece-jointe.model';

export interface Document {
  id?: number;
  dateReception?: string;
  dateEmission?: string;
  typeDocument: string;
  reference: string;
  nature: string;
  expediteur: string;
  destinataire?: string; 
  modeReception?: string;
  modeExpedition?: string;
  priorite: string;
  statut: string;
  etat?: string; 
  pieceJointesCount?: number;
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

  pieceJointes: any[] = [];
  selectedFiles: File[] = [];
  filePreviewUrl: string | null = null;
  fileDescription = '';
  fileUploadProgress = 0;
  isUploading = false;
  isEditMode = false;
  today = '';

  constructor(private documentService: DocumentService) {
    this.today = this.getTodayDate();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['currentDocument'] && this.currentDocument) {
      this.isEditMode = !!this.currentDocument.id;

      this.selectedFiles = null;
      this.filePreviewUrl = null;
      this.fileDescription = '';
      this.pieceJointes = [];

      if (this.isEditMode && this.currentDocument.id) {
        this.loadPieceJointes(this.currentDocument.id);
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
    const file: File = event.target.files[0];
    if (!file) return;

    if (file.size > 10 * 1024 * 1024) {
      alert('Fichier trop volumineux (max 10MB)');
      return;
    }

    this.selectedFiles = [file];

    if (file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = e => this.filePreviewUrl = (e.target as any).result;
      reader.readAsDataURL(file);
    } else {
      this.filePreviewUrl = null;
    }
  }

  removeFile() {
    this.selectedFiles = null;
    this.filePreviewUrl = null;
    this.fileDescription = '';
  }

 saveDocument() {
  if (!this.currentDocument) return;

  const data: any = { ...this.currentDocument };

  if (data.dateReception) data.dateReception = this.formatDate(data.dateReception);
  if (data.dateEmission) data.dateEmission = this.formatDate(data.dateEmission);

  if (this.type === 'entrant') {
    data.modeReception = data.modeReception || 'EMAIL';
  } else {
    data.modeExpedition = data.modeExpedition || 'EMAIL';
  }

  data.statut = data.statut || 'NOUVEAU';
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

  const uploads = this.selectedFiles.map(file => {
    return this.type === 'entrant'
      ? this.documentService.uploadPieceJointeEntrant(docId, file, this.fileDescription).toPromise()
      : this.documentService.uploadPieceJointeSortant(docId, file, this.fileDescription).toPromise();
  });

  Promise.all(uploads)
    .then(() => {
      this.loadPieceJointes(docId);
      this.resetFile();
      this.finishSave(this.currentDocument); // ⭐ IMPORTANT
    })
    .catch(err => {
      console.error(err);
      alert("Erreur upload pièces jointes");
      this.finishSave(this.currentDocument);
    });

} private afterSave(doc: Document) {
  this.isUploading = false;        // Débloque le bouton
  this.fileUploadProgress = 0;
  this.savedDocument.emit(doc);
  this.close();
}

close() {
  this.showModal = false;
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
  
}
