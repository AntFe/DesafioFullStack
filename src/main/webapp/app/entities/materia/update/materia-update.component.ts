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
import { IAula } from 'app/entities/aula/aula.model';
import { AulaService } from 'app/entities/aula/service/aula.service';
import { IProfessor } from 'app/entities/professor/professor.model';
import { ProfessorService } from 'app/entities/professor/service/professor.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { MateriaService } from '../service/materia.service';
import { IMateria } from '../materia.model';
import { MateriaFormGroup, MateriaFormService } from './materia-form.service';

@Component({
  selector: 'jhi-materia-update',
  templateUrl: './materia-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MateriaUpdateComponent implements OnInit {
  isSaving = false;
  materia: IMateria | null = null;

  aulasSharedCollection: IAula[] = [];
  professorsSharedCollection: IProfessor[] = [];
  cursosSharedCollection: ICurso[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected materiaService = inject(MateriaService);
  protected materiaFormService = inject(MateriaFormService);
  protected aulaService = inject(AulaService);
  protected professorService = inject(ProfessorService);
  protected cursoService = inject(CursoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MateriaFormGroup = this.materiaFormService.createMateriaFormGroup();

  compareAula = (o1: IAula | null, o2: IAula | null): boolean => this.aulaService.compareAula(o1, o2);

  compareProfessor = (o1: IProfessor | null, o2: IProfessor | null): boolean => this.professorService.compareProfessor(o1, o2);

  compareCurso = (o1: ICurso | null, o2: ICurso | null): boolean => this.cursoService.compareCurso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materia }) => {
      this.materia = materia;
      if (materia) {
        this.updateForm(materia);
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
    const materia = this.materiaFormService.getMateria(this.editForm);
    if (materia.id !== null) {
      this.subscribeToSaveResponse(this.materiaService.update(materia));
    } else {
      this.subscribeToSaveResponse(this.materiaService.create(materia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMateria>>): void {
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

  protected updateForm(materia: IMateria): void {
    this.materia = materia;
    this.materiaFormService.resetForm(this.editForm, materia);

    this.aulasSharedCollection = this.aulaService.addAulaToCollectionIfMissing<IAula>(this.aulasSharedCollection, ...(materia.aulas ?? []));
    this.professorsSharedCollection = this.professorService.addProfessorToCollectionIfMissing<IProfessor>(
      this.professorsSharedCollection,
      materia.professor,
    );
    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing<ICurso>(
      this.cursosSharedCollection,
      ...(materia.cursos ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aulaService
      .query()
      .pipe(map((res: HttpResponse<IAula[]>) => res.body ?? []))
      .pipe(map((aulas: IAula[]) => this.aulaService.addAulaToCollectionIfMissing<IAula>(aulas, ...(this.materia?.aulas ?? []))))
      .subscribe((aulas: IAula[]) => (this.aulasSharedCollection = aulas));

    this.professorService
      .query()
      .pipe(map((res: HttpResponse<IProfessor[]>) => res.body ?? []))
      .pipe(
        map((professors: IProfessor[]) =>
          this.professorService.addProfessorToCollectionIfMissing<IProfessor>(professors, this.materia?.professor),
        ),
      )
      .subscribe((professors: IProfessor[]) => (this.professorsSharedCollection = professors));

    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing<ICurso>(cursos, ...(this.materia?.cursos ?? []))))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));
  }
}
