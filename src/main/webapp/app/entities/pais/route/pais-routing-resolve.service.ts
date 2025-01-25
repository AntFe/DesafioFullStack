import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPais } from '../pais.model';
import { PaisService } from '../service/pais.service';

const paisResolve = (route: ActivatedRouteSnapshot): Observable<null | IPais> => {
  const id = route.params.id;
  if (id) {
    return inject(PaisService)
      .find(id)
      .pipe(
        mergeMap((pais: HttpResponse<IPais>) => {
          if (pais.body) {
            return of(pais.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default paisResolve;
