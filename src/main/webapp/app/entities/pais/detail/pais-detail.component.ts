import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPais } from '../pais.model';

@Component({
  selector: 'jhi-pais-detail',
  templateUrl: './pais-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PaisDetailComponent {
  pais = input<IPais | null>(null);

  previousState(): void {
    window.history.back();
  }
}
