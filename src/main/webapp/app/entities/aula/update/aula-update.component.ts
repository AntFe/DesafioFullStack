import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMateria } from 'app/entities/materia/materia.model';
import { MateriaService } from 'app/entities/materia/service/materia.service';
import { AulaService } from '../service/aula.service';
import { IAula } from '../aula.model';
import { AulaFormGroup, AulaFormService } from './aula-form.service';

@Component({
  selector: 'jhi-aula-update',
  templateUrl: './aula-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AulaUpdateComponent implements OnInit {
  isSaving = false;
  aula: IAula | null = null;

  materiasSharedCollection: IMateria[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected aulaService = inject(AulaService);
  protected aulaFormService = inject(AulaFormService);
  protected materiaService = inject(MateriaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AulaFormGroup = this.aulaFormService.createAulaFormGroup();

  compareMateria = (o1: IMateria | null, o2: IMateria | null): boolean => this.materiaService.compareMateria(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aula }) => {
      this.aula = aula;
      if (aula) {
        this.updateForm(aula);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('plataformaAulasApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aula = this.aulaFormService.getAula(this.editForm);
    if (aula.id !== null) {
      this.subscribeToSaveResponse(this.aulaService.update(aula));
    } else {
      this.subscribeToSaveResponse(this.aulaService.create(aula));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAula>>): void {
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

  protected updateForm(aula: IAula): void {
    this.aula = aula;
    this.aulaFormService.resetForm(this.editForm, aula);

    this.materiasSharedCollection = this.materiaService.addMateriaToCollectionIfMissing<IMateria>(
      this.materiasSharedCollection,
      ...(aula.materias ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materiaService
      .query()
      .pipe(map((res: HttpResponse<IMateria[]>) => res.body ?? []))
      .pipe(
        map((materias: IMateria[]) =>
          this.materiaService.addMateriaToCollectionIfMissing<IMateria>(materias, ...(this.aula?.materias ?? [])),
        ),
      )
      .subscribe((materias: IMateria[]) => (this.materiasSharedCollection = materias));
  }
}
