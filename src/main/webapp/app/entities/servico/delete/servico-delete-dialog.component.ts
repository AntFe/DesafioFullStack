import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServico } from '../servico.model';
import { ServicoService } from '../service/servico.service';

@Component({
  templateUrl: './servico-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServicoDeleteDialogComponent {
  servico?: IServico;

  protected servicoService = inject(ServicoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.servicoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
