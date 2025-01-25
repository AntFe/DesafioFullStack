import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAula, NewAula } from '../aula.model';

export type PartialUpdateAula = Partial<IAula> & Pick<IAula, 'id'>;

export type EntityResponseType = HttpResponse<IAula>;
export type EntityArrayResponseType = HttpResponse<IAula[]>;

@Injectable({ providedIn: 'root' })
export class AulaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aulas');

  create(aula: NewAula): Observable<EntityResponseType> {
    return this.http.post<IAula>(this.resourceUrl, aula, { observe: 'response' });
  }

  update(aula: IAula): Observable<EntityResponseType> {
    return this.http.put<IAula>(`${this.resourceUrl}/${this.getAulaIdentifier(aula)}`, aula, { observe: 'response' });
  }

  partialUpdate(aula: PartialUpdateAula): Observable<EntityResponseType> {
    return this.http.patch<IAula>(`${this.resourceUrl}/${this.getAulaIdentifier(aula)}`, aula, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAula>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAula[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAulaIdentifier(aula: Pick<IAula, 'id'>): number {
    return aula.id;
  }

  compareAula(o1: Pick<IAula, 'id'> | null, o2: Pick<IAula, 'id'> | null): boolean {
    return o1 && o2 ? this.getAulaIdentifier(o1) === this.getAulaIdentifier(o2) : o1 === o2;
  }

  addAulaToCollectionIfMissing<Type extends Pick<IAula, 'id'>>(
    aulaCollection: Type[],
    ...aulasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aulas: Type[] = aulasToCheck.filter(isPresent);
    if (aulas.length > 0) {
      const aulaCollectionIdentifiers = aulaCollection.map(aulaItem => this.getAulaIdentifier(aulaItem));
      const aulasToAdd = aulas.filter(aulaItem => {
        const aulaIdentifier = this.getAulaIdentifier(aulaItem);
        if (aulaCollectionIdentifiers.includes(aulaIdentifier)) {
          return false;
        }
        aulaCollectionIdentifiers.push(aulaIdentifier);
        return true;
      });
      return [...aulasToAdd, ...aulaCollection];
    }
    return aulaCollection;
  }
}
