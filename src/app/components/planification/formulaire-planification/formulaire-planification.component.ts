import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-formulaire-planification',
  templateUrl: './formulaire-planification.component.html'
})
export class FormulairePlanificationComponent {

  @Input() show = false;
  @Input() isEdit = false;
  @Input() form: any = {};
  @Input() employes: any[] = [];

  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();

  selectedFile: File | null = null;

  closeModal() {
    this.close.emit();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  submit() {
    this.save.emit({
      ...this.form,
      file: this.selectedFile
    });
  }
}