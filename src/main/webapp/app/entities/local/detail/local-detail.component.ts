import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ILocal } from '../local.model';

@Component({
  selector: 'jhi-local-detail',
  templateUrl: './local-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LocalDetailComponent {
  local = input<ILocal | null>(null);

  previousState(): void {
    window.history.back();
  }
}
