import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMateria } from '../materia.model';
import { MateriaService } from '../service/materia.service';

const materiaResolve = (route: ActivatedRouteSnapshot): Observable<null | IMateria> => {
  const id = route.params.id;
  if (id) {
    return inject(MateriaService)
      .find(id)
      .pipe(
        mergeMap((materia: HttpResponse<IMateria>) => {
          if (materia.body) {
            return of(materia.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default materiaResolve;
