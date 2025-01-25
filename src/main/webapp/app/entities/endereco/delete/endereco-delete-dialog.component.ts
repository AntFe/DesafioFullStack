import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEndereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';

@Component({
  templateUrl: './endereco-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EnderecoDeleteDialogComponent {
  endereco?: IEndereco;

  protected enderecoService = inject(EnderecoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enderecoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
