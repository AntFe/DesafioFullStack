import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICurso } from '../curso.model';

@Component({
  selector: 'jhi-curso-detail',
  templateUrl: './curso-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CursoDetailComponent {
  curso = input<ICurso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
