import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';
import { IEndereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';
import { EnderecoFormGroup, EnderecoFormService } from './endereco-form.service';

@Component({
  selector: 'jhi-endereco-update',
  templateUrl: './endereco-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnderecoUpdateComponent implements OnInit {
  isSaving = false;
  endereco: IEndereco | null = null;

  paisCollection: IPais[] = [];

  protected enderecoService = inject(EnderecoService);
  protected enderecoFormService = inject(EnderecoFormService);
  protected paisService = inject(PaisService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EnderecoFormGroup = this.enderecoFormService.createEnderecoFormGroup();

  comparePais = (o1: IPais | null, o2: IPais | null): boolean => this.paisService.comparePais(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endereco }) => {
      this.endereco = endereco;
      if (endereco) {
        this.updateForm(endereco);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const endereco = this.enderecoFormService.getEndereco(this.editForm);
    if (endereco.id !== null) {
      this.subscribeToSaveResponse(this.enderecoService.update(endereco));
    } else {
      this.subscribeToSaveResponse(this.enderecoService.create(endereco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndereco>>): void {
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

  protected updateForm(endereco: IEndereco): void {
    this.endereco = endereco;
    this.enderecoFormService.resetForm(this.editForm, endereco);

    this.paisCollection = this.paisService.addPaisToCollectionIfMissing<IPais>(this.paisCollection, endereco.pais);
  }

  protected loadRelationshipsOptions(): void {
    this.paisService
      .query({ filter: 'endereco-is-null' })
      .pipe(map((res: HttpResponse<IPais[]>) => res.body ?? []))
      .pipe(map((pais: IPais[]) => this.paisService.addPaisToCollectionIfMissing<IPais>(pais, this.endereco?.pais)))
      .subscribe((pais: IPais[]) => (this.paisCollection = pais));
  }
}
