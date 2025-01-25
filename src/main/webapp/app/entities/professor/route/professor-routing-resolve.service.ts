import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfessor } from '../professor.model';
import { ProfessorService } from '../service/professor.service';

const professorResolve = (route: ActivatedRouteSnapshot): Observable<null | IProfessor> => {
  const id = route.params.id;
  if (id) {
    return inject(ProfessorService)
      .find(id)
      .pipe(
        mergeMap((professor: HttpResponse<IProfessor>) => {
          if (professor.body) {
            return of(professor.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default professorResolve;
