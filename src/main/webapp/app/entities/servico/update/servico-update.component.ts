import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ServicoService } from '../service/servico.service';
import { IServico } from '../servico.model';
import { ServicoFormGroup, ServicoFormService } from './servico-form.service';

@Component({
  selector: 'jhi-servico-update',
  templateUrl: './servico-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServicoUpdateComponent implements OnInit {
  isSaving = false;
  servico: IServico | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected servicoService = inject(ServicoService);
  protected servicoFormService = inject(ServicoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServicoFormGroup = this.servicoFormService.createServicoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servico }) => {
      this.servico = servico;
      if (servico) {
        this.updateForm(servico);
      }
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
    const servico = this.servicoFormService.getServico(this.editForm);
    if (servico.id !== null) {
      this.subscribeToSaveResponse(this.servicoService.update(servico));
    } else {
      this.subscribeToSaveResponse(this.servicoService.create(servico));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServico>>): void {
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

  protected updateForm(servico: IServico): void {
    this.servico = servico;
    this.servicoFormService.resetForm(this.editForm, servico);
  }
}
