import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAula } from '../aula.model';
import { AulaService } from '../service/aula.service';

const aulaResolve = (route: ActivatedRouteSnapshot): Observable<null | IAula> => {
  const id = route.params.id;
  if (id) {
    return inject(AulaService)
      .find(id)
      .pipe(
        mergeMap((aula: HttpResponse<IAula>) => {
          if (aula.body) {
            return of(aula.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aulaResolve;
