import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPais, NewPais } from '../pais.model';

export type PartialUpdatePais = Partial<IPais> & Pick<IPais, 'id'>;

export type EntityResponseType = HttpResponse<IPais>;
export type EntityArrayResponseType = HttpResponse<IPais[]>;

@Injectable({ providedIn: 'root' })
export class PaisService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pais');

  create(pais: NewPais): Observable<EntityResponseType> {
    return this.http.post<IPais>(this.resourceUrl, pais, { observe: 'response' });
  }

  update(pais: IPais): Observable<EntityResponseType> {
    return this.http.put<IPais>(`${this.resourceUrl}/${this.getPaisIdentifier(pais)}`, pais, { observe: 'response' });
  }

  partialUpdate(pais: PartialUpdatePais): Observable<EntityResponseType> {
    return this.http.patch<IPais>(`${this.resourceUrl}/${this.getPaisIdentifier(pais)}`, pais, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPais>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPais[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaisIdentifier(pais: Pick<IPais, 'id'>): number {
    return pais.id;
  }

  comparePais(o1: Pick<IPais, 'id'> | null, o2: Pick<IPais, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaisIdentifier(o1) === this.getPaisIdentifier(o2) : o1 === o2;
  }

  addPaisToCollectionIfMissing<Type extends Pick<IPais, 'id'>>(
    paisCollection: Type[],
    ...paisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pais: Type[] = paisToCheck.filter(isPresent);
    if (pais.length > 0) {
      const paisCollectionIdentifiers = paisCollection.map(paisItem => this.getPaisIdentifier(paisItem));
      const paisToAdd = pais.filter(paisItem => {
        const paisIdentifier = this.getPaisIdentifier(paisItem);
        if (paisCollectionIdentifiers.includes(paisIdentifier)) {
          return false;
        }
        paisCollectionIdentifiers.push(paisIdentifier);
        return true;
      });
      return [...paisToAdd, ...paisCollection];
    }
    return paisCollection;
  }
}
