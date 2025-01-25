import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEndereco } from '../endereco.model';

@Component({
  selector: 'jhi-endereco-detail',
  templateUrl: './endereco-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EnderecoDetailComponent {
  endereco = input<IEndereco | null>(null);

  previousState(): void {
    window.history.back();
  }
}
