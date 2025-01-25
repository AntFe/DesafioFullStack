import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServico, NewServico } from '../servico.model';

export type PartialUpdateServico = Partial<IServico> & Pick<IServico, 'id'>;

type RestOf<T extends IServico | NewServico> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestServico = RestOf<IServico>;

export type NewRestServico = RestOf<NewServico>;

export type PartialUpdateRestServico = RestOf<PartialUpdateServico>;

export type EntityResponseType = HttpResponse<IServico>;
export type EntityArrayResponseType = HttpResponse<IServico[]>;

@Injectable({ providedIn: 'root' })
export class ServicoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/servicos');

  create(servico: NewServico): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servico);
    return this.http
      .post<RestServico>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(servico: IServico): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servico);
    return this.http
      .put<RestServico>(`${this.resourceUrl}/${this.getServicoIdentifier(servico)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(servico: PartialUpdateServico): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servico);
    return this.http
      .patch<RestServico>(`${this.resourceUrl}/${this.getServicoIdentifier(servico)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServico>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServico[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServicoIdentifier(servico: Pick<IServico, 'id'>): number {
    return servico.id;
  }

  compareServico(o1: Pick<IServico, 'id'> | null, o2: Pick<IServico, 'id'> | null): boolean {
    return o1 && o2 ? this.getServicoIdentifier(o1) === this.getServicoIdentifier(o2) : o1 === o2;
  }

  addServicoToCollectionIfMissing<Type extends Pick<IServico, 'id'>>(
    servicoCollection: Type[],
    ...servicosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const servicos: Type[] = servicosToCheck.filter(isPresent);
    if (servicos.length > 0) {
      const servicoCollectionIdentifiers = servicoCollection.map(servicoItem => this.getServicoIdentifier(servicoItem));
      const servicosToAdd = servicos.filter(servicoItem => {
        const servicoIdentifier = this.getServicoIdentifier(servicoItem);
        if (servicoCollectionIdentifiers.includes(servicoIdentifier)) {
          return false;
        }
        servicoCollectionIdentifiers.push(servicoIdentifier);
        return true;
      });
      return [...servicosToAdd, ...servicoCollection];
    }
    return servicoCollection;
  }

  protected convertDateFromClient<T extends IServico | NewServico | PartialUpdateServico>(servico: T): RestOf<T> {
    return {
      ...servico,
      startDate: servico.startDate?.toJSON() ?? null,
      endDate: servico.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServico: RestServico): IServico {
    return {
      ...restServico,
      startDate: restServico.startDate ? dayjs(restServico.startDate) : undefined,
      endDate: restServico.endDate ? dayjs(restServico.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServico>): HttpResponse<IServico> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServico[]>): HttpResponse<IServico[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
