import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServico } from '../servico.model';
import { ServicoService } from '../service/servico.service';

const servicoResolve = (route: ActivatedRouteSnapshot): Observable<null | IServico> => {
  const id = route.params.id;
  if (id) {
    return inject(ServicoService)
      .find(id)
      .pipe(
        mergeMap((servico: HttpResponse<IServico>) => {
          if (servico.body) {
            return of(servico.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default servicoResolve;
