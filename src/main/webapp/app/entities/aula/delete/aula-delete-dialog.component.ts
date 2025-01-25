import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAula } from '../aula.model';
import { AulaService } from '../service/aula.service';

@Component({
  templateUrl: './aula-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AulaDeleteDialogComponent {
  aula?: IAula;

  protected aulaService = inject(AulaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aulaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
