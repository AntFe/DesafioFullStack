import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServico } from 'app/entities/servico/servico.model';
import { ServicoService } from 'app/entities/servico/service/servico.service';
import { IProfessor } from '../professor.model';
import { ProfessorService } from '../service/professor.service';
import { ProfessorFormGroup, ProfessorFormService } from './professor-form.service';

@Component({
  selector: 'jhi-professor-update',
  templateUrl: './professor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfessorUpdateComponent implements OnInit {
  isSaving = false;
  professor: IProfessor | null = null;

  servicosCollection: IServico[] = [];

  protected professorService = inject(ProfessorService);
  protected professorFormService = inject(ProfessorFormService);
  protected servicoService = inject(ServicoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfessorFormGroup = this.professorFormService.createProfessorFormGroup();

  compareServico = (o1: IServico | null, o2: IServico | null): boolean => this.servicoService.compareServico(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professor }) => {
      this.professor = professor;
      if (professor) {
        this.updateForm(professor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professor = this.professorFormService.getProfessor(this.editForm);
    if (professor.id !== null) {
      this.subscribeToSaveResponse(this.professorService.update(professor));
    } else {
      this.subscribeToSaveResponse(this.professorService.create(professor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfessor>>): void {
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

  protected updateForm(professor: IProfessor): void {
    this.professor = professor;
    this.professorFormService.resetForm(this.editForm, professor);

    this.servicosCollection = this.servicoService.addServicoToCollectionIfMissing<IServico>(this.servicosCollection, professor.servico);
  }

  protected loadRelationshipsOptions(): void {
    this.servicoService
      .query({ filter: 'professor-is-null' })
      .pipe(map((res: HttpResponse<IServico[]>) => res.body ?? []))
      .pipe(map((servicos: IServico[]) => this.servicoService.addServicoToCollectionIfMissing<IServico>(servicos, this.professor?.servico)))
      .subscribe((servicos: IServico[]) => (this.servicosCollection = servicos));
  }
}
