import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMateria } from '../materia.model';
import { MateriaService } from '../service/materia.service';

@Component({
  templateUrl: './materia-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MateriaDeleteDialogComponent {
  materia?: IMateria;

  protected materiaService = inject(MateriaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materiaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
