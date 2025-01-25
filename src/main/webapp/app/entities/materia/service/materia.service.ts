import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMateria, NewMateria } from '../materia.model';

export type PartialUpdateMateria = Partial<IMateria> & Pick<IMateria, 'id'>;

export type EntityResponseType = HttpResponse<IMateria>;
export type EntityArrayResponseType = HttpResponse<IMateria[]>;

@Injectable({ providedIn: 'root' })
export class MateriaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/materias');

  create(materia: NewMateria): Observable<EntityResponseType> {
    return this.http.post<IMateria>(this.resourceUrl, materia, { observe: 'response' });
  }

  update(materia: IMateria): Observable<EntityResponseType> {
    return this.http.put<IMateria>(`${this.resourceUrl}/${this.getMateriaIdentifier(materia)}`, materia, { observe: 'response' });
  }

  partialUpdate(materia: PartialUpdateMateria): Observable<EntityResponseType> {
    return this.http.patch<IMateria>(`${this.resourceUrl}/${this.getMateriaIdentifier(materia)}`, materia, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMateria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMateria[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMateriaIdentifier(materia: Pick<IMateria, 'id'>): number {
    return materia.id;
  }

  compareMateria(o1: Pick<IMateria, 'id'> | null, o2: Pick<IMateria, 'id'> | null): boolean {
    return o1 && o2 ? this.getMateriaIdentifier(o1) === this.getMateriaIdentifier(o2) : o1 === o2;
  }

  addMateriaToCollectionIfMissing<Type extends Pick<IMateria, 'id'>>(
    materiaCollection: Type[],
    ...materiasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materias: Type[] = materiasToCheck.filter(isPresent);
    if (materias.length > 0) {
      const materiaCollectionIdentifiers = materiaCollection.map(materiaItem => this.getMateriaIdentifier(materiaItem));
      const materiasToAdd = materias.filter(materiaItem => {
        const materiaIdentifier = this.getMateriaIdentifier(materiaItem);
        if (materiaCollectionIdentifiers.includes(materiaIdentifier)) {
          return false;
        }
        materiaCollectionIdentifiers.push(materiaIdentifier);
        return true;
      });
      return [...materiasToAdd, ...materiaCollection];
    }
    return materiaCollection;
  }
}
