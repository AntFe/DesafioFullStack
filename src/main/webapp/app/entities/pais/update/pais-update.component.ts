import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILocal } from 'app/entities/local/local.model';
import { LocalService } from 'app/entities/local/service/local.service';
import { IPais } from '../pais.model';
import { PaisService } from '../service/pais.service';
import { PaisFormGroup, PaisFormService } from './pais-form.service';

@Component({
  selector: 'jhi-pais-update',
  templateUrl: './pais-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaisUpdateComponent implements OnInit {
  isSaving = false;
  pais: IPais | null = null;

  localsCollection: ILocal[] = [];

  protected paisService = inject(PaisService);
  protected paisFormService = inject(PaisFormService);
  protected localService = inject(LocalService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaisFormGroup = this.paisFormService.createPaisFormGroup();

  compareLocal = (o1: ILocal | null, o2: ILocal | null): boolean => this.localService.compareLocal(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pais }) => {
      this.pais = pais;
      if (pais) {
        this.updateForm(pais);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pais = this.paisFormService.getPais(this.editForm);
    if (pais.id !== null) {
      this.subscribeToSaveResponse(this.paisService.update(pais));
    } else {
      this.subscribeToSaveResponse(this.paisService.create(pais));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPais>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pais: IPais): void {
    this.pais = pais;
    this.paisFormService.resetForm(this.editForm, pais);

    this.localsCollection = this.localService.addLocalToCollectionIfMissing<ILocal>(this.localsCollection, pais.local);
  }

  protected loadRelationshipsOptions(): void {
    this.localService
      .query({ filter: 'pais-is-null' })
      .pipe(map((res: HttpResponse<ILocal[]>) => res.body ?? []))
      .pipe(map((locals: ILocal[]) => this.localService.addLocalToCollectionIfMissing<ILocal>(locals, this.pais?.local)))
      .subscribe((locals: ILocal[]) => (this.localsCollection = locals));
  }
}
